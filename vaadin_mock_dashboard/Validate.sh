#!/bin/bash

# Exit immediately on any error
set -euo pipefail

# Configuration variables
readonly SCRIPT_NAME="$(basename "$0")"
readonly DEFAULT_TRUSTSTORE="ca_keystore.crt"
readonly DEFAULT_TRUSTSTORE_PASSWORD="password"
readonly APP_LOGS_BASE_DIR="${APP_LOGS_BASE_DIR:-/app/logs}"

# Usage function
usage() {
    cat << EOF
Usage: $SCRIPT_NAME <ACTION_MODE> <JAR_FILE>

ACTION_MODE: START, STOP, or RESTART
JAR_FILE:    Name of the JAR file to manage

Examples:
  $SCRIPT_NAME START web-visu-2025-4.0.jar
  $SCRIPT_NAME STOP web-visu-2025-4.0.jar
  $SCRIPT_NAME RESTART web-visu-2025-4.0.jar
EOF
    exit 1
}

# Logging function
log() {
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] $*" >&2
}

# Error logging function
log_error() {
    log "ERROR: $*"
}

# Validate input parameters
validate_parameters() {
    if [[ $# -ne 2 ]]; then
        log_error "Invalid number of parameters"
        usage
    fi

    local action_mode="$1"
    local jar_file="$2"

    # Validate action mode
    case "${action_mode}" in
        START|STOP|RESTART) ;;
        *) log_error "Invalid action mode: $action_mode"; usage ;;
    esac

    # Validate JAR file exists and is readable
    if [[ ! -f "$jar_file" ]]; then
        log_error "JAR file not found: $jar_file"
        exit 1
    fi

    if [[ ! -r "$jar_file" ]]; then
        log_error "JAR file not readable: $jar_file"
        exit 1
    fi
}

# Get application name from JAR filename
get_app_name() {
    local jar_file="$1"
    basename "$jar_file" | cut -d '-' -f 1
}

# Get process IDs for the application
get_process_ids() {
    local jar_file="$1"
    pgrep -f "$(basename "$jar_file")" || echo ""
}

# Stop application gracefully
stop_app() {
    local jar_file="$1"
    local app_name="$2"
    local max_wait_time=30
    local wait_interval=2
    
    log "Stopping $app_name application..."
    
    local process_ids
    process_ids=$(get_process_ids "$jar_file")
    
    if [[ -z "$process_ids" ]]; then
        log "Application $app_name is not running"
        return 0
    fi

    log "Found running processes: $process_ids"
    
    # Send SIGTERM first (graceful shutdown)
    echo "$process_ids" | xargs kill
    
    # Wait for processes to terminate gracefully
    local waited=0
    while [[ $waited -lt $max_wait_time ]] && [[ -n $(get_process_ids "$jar_file") ]]; do
        sleep $wait_interval
        waited=$((waited + wait_interval))
        log "Waiting for graceful shutdown... (${waited}s)"
    done
    
    # If processes still running, force kill with SIGKILL
    local remaining_pids
    remaining_pids=$(get_process_ids "$jar_file")
    if [[ -n "$remaining_pids" ]]; then
        log "Forcing shutdown of processes: $remaining_pids"
        echo "$remaining_pids" | xargs kill -9
        
        # Verify all processes are killed
        sleep 2
        if [[ -n $(get_process_ids "$jar_file") ]]; then
            log_error "Failed to stop all processes for $app_name"
            return 1
        fi
    fi
    
    log "Application $app_name stopped successfully"
    return 0
}

# Start application
start_app() {
    local jar_file="$1"
    local app_name="$2"
    
    log "Starting $app_name application..."
    
    # Check if application is already running
    local existing_pids
    existing_pids=$(get_process_ids "$jar_file")
    if [[ -n "$existing_pids" ]]; then
        log_error "Application $app_name is already running with PIDs: $existing_pids"
        return 1
    fi
    
    # Create logs directory with proper permissions
    local app_logs_dir="${APP_LOGS_BASE_DIR}/${app_name}"
    if ! mkdir -p "$app_logs_dir"; then
        log_error "Failed to create logs directory: $app_logs_dir"
        return 1
    fi
    
    # Set proper permissions for logs directory
    chmod 755 "$app_logs_dir"
    
    # Prepare log files
    local log_file="${app_logs_dir}/${app_name}.log"
    local err_file="${app_logs_dir}/${app_name}.err"
    
    # Rotate logs if they exist
    for file in "$log_file" "$err_file"; do
        if [[ -f "$file" ]]; then
            mv "$file" "${file}.old"
            gzip "${file}.old" 2>/dev/null || true
        fi
    done
    
    # Build Java command
    local java_cmd=(
        java
        -Djavax.net.ssl.trustStore="$DEFAULT_TRUSTSTORE"
        -Djavax.net.ssl.trustStorePassword="$DEFAULT_TRUSTSTORE_PASSWORD"
        -Dspring.profiles.active=pt
        -Denv.database=horsprod
        -Dtest-email-only=true
        -Dtype-metrics=test
        -Dliste.dba=pascal.thomas@lcl.fr
        -Dlibelle.message.dba=TEST-ENVOI
        -Dkeep.referent=true
        -jar "$jar_file"
        --spring.config.location=classpath:/application-pt.properties
    )
    
    # Start application
    if ! nohup "${java_cmd[@]}" > "$log_file" 2> "$err_file" & then
        log_error "Failed to start application $app_name"
        return 1
    fi
    
    # Get the PID of the started process
    local new_pid=$!
    log "Application started with PID: $new_pid"
    
    # Wait a moment and verify it's still running
    sleep 5
    if ! kill -0 "$new_pid" 2>/dev/null; then
        log_error "Application process died shortly after startup. Check logs: $err_file"
        return 1
    fi
    
    log "Application $app_name started successfully (PID: $new_pid)"
    log "Log files: $log_file, $err_file"
}

# Main execution
main() {
    local action_mode="$1"
    local jar_file="$2"
    local app_name
    
    # Validate parameters first
    validate_parameters "$action_mode" "$jar_file"
    
    # Get absolute path to JAR file
    jar_file="$(realpath "$jar_file")"
    app_name=$(get_app_name "$jar_file")
    
    log "Managing application: $app_name (JAR: $jar_file)"
    
    case "$action_mode" in
        START)
            start_app "$jar_file" "$app_name"
            ;;
        STOP)
            stop_app "$jar_file" "$app_name"
            ;;
        RESTART)
            if stop_app "$jar_file" "$app_name"; then
                sleep 2
                start_app "$jar_file" "$app_name"
            else
                log_error "Failed to stop application during restart"
                exit 1
            fi
            ;;
    esac
}

# Check if script is being sourced or executed directly
if [[ "${BASH_SOURCE[0]}" == "${0}" ]]; then
    # Set environment variables at the beginning
    export LD_LIBRARY_PATH=""
    export PATH="/app/list/Data/devops/jdk-17.0.2/bin:/usr/local/bin:/usr/bin:/usr/local/sbin:/usr/sbin"
    
    # Run main function with all arguments
    main "$@"
fi


----
# Start application
./script.sh START web-visu-2025-4.0.jar

# Stop application  
./script.sh STOP web-visu-2025-4.0.jar

# Restart application
./script.sh RESTART web-visu-2025-4.0.jar

