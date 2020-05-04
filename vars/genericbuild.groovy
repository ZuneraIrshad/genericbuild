def call(Map config-[:]){
node { 
	stage('SCM') {
		echo 'Gathering code from version control'
		git branch: '${branch}', url: 'https://github.com/ZuneraIrshad/genericbuild.git'
	}

	stage('Build') {
		try{
			echo 'Building...'
			//sh 'add npm task'
			echo 'Building New Feature'
			releasenotes(changes: "true")
		}catch(ex){
			echo 'Something went wrong...'
			echo ex.toString();
			currentBuild.result = 'FAILURE'
		}
		finally{
			// cleanup
		}
	}
	stage('Test') {
		echo 'Testing...' // to-do - add steps for all these.. 
	}
	stage('Code Quality') {
		echo 'Running Sonar...'
	}
	stage('Lint/Format') {
		echo 'Code Linting...'
	}
	stage('Push Artifacts'){
		echo 'Pushing to Artifcatory...'
	}
  }
}
