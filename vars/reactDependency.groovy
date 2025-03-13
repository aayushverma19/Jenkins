def call (string repdir) {
sh "npm audit --output-html>'${repdir}'  || true"
}

// REPORT_FILE="${repdir}"
