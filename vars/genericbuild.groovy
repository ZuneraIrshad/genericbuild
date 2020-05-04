def call(Map config=[:]){
node('DOTNETCORE'){
	stage('SCM'){
		echo 'Gathering code from version control'
		//git branch: '${branch}', url: 'https://github.com/ZuneraIrshad/DockerJenkins.git'
		checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[url: 'https://github.com/ZuneraIrshad/DockerJenkins.git']]]);
	}
	
	stage('Build'){
		try{
		  echo 'Building...'
		  //sh 'dotnet --version'
		  sh 'dotnet build ConsoleApp1'
		  echo 'Building New Feature'
		  releasenotes(changes: "true")
		  // true == note include changes log
		}catch(ex){
		  echo 'something went wrong'
		  echo ex.toString();
		  currentBuild.result = 'FAILURE'
		} finally{
		  //archiveArtifacts artifacts: 'ConsoleApp1/*.*'
 		}
		//cleanup
	}
	stage('Test'){
		echo 'Execute Unit Tests'
	}
	stage('Package'){
		echo 'Zip it up'
	}
	stage('Deploy'){
		echo 'Push to Artifactory'
	}
	stage('Archive'){
		archiveArtifacts artifacts: 'ConsoleApp1/*.*'
	}
}
