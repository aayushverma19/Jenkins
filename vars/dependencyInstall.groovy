
def call(String depCheckVersion) {
    sh """
        DEP_CHECK_DIR="${WORKSPACE}/dependency-check"

        if [ ! -d "$DEP_CHECK_DIR" ]; then
            curl -sLO https://github.com/jeremylong/DependencyCheck/releases/download/v${depCheckVersion}/dependency-check-${depCheckVersion}-release.zip
            unzip dependency-check-${depCheckVersion}-release.zip
        fi

        ${WORKSPACE}/dependency-check/bin/dependency-check.sh --version
    """
}
