def call () {
  sh '''

      if [ ! -d "$DEP_CHECK_DIR" ]; then
          curl -sLO https://github.com/jeremylong/DependencyCheck/releases/download/v$DEP_CHECK_VERSION/dependency-check-$DEP_CHECK_VERSION-release.zip
          unzip dependency-check-$DEP_CHECK_VERSION-release.zip
      fi
      
      # Verify installation
      $DEP_CHECK_DIR/bin/dependency-check.sh --version
      '''
}
