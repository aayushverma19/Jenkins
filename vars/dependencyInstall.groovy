def call(String depCheckVersion, String depdir) {
    sh """
        DEP_CHECK_DIR="${depdir}"

        if [ ! -d "\${DEP_CHECK_DIR}" ]; then
            echo "Directory does not exist, downloading DependencyCheck..."
            curl -sLO https://github.com/jeremylong/DependencyCheck/releases/download/v${depCheckVersion}/dependency-check-${depCheckVersion}-release.zip
            unzip dependency-check-${depCheckVersion}-release.zip
        fi

        \${DEP_CHECK_DIR}/bin/dependency-check.sh --version
    """
}
