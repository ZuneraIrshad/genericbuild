class PluginManager{
  Set pluginIds;
  //Set of installed plugins 
  
  PluginManager(){
  	File whiteList = new File("/var/jenkins_home/userContent/whitelist.txt");
    pluginIds = whiteList.readLines();
  
  }
  
  Set installedPluginIds(){
  	return (Set)(Jenkins.instance.pluginManager.plugins.shortName);
  }
  // minus from the install sets - sort to make it clear 
  Set installedNotInWhitelist(){
  	return installedPluginIds().minus(pluginIds).sort();
  }
  
  Set whitelistedNotInstalled(){
  	return pluginIds.minus(installedPluginIds());
  }
  
  void installWhitelist(){
    for (pluginId in whitelistedNotInstalled()){
    	PluginCheck plugin = new PluginCheck(pluginId, "9");
      
      	plugin.install();
    }
  
  }
  
  
  void disableNotWhitelisted(){
    for (pluginId in installedNotInWhitelist()){
    	PluginCheck plugin = new PluginCheck(pluginId, "9");
      
      if (plugin.enabled){
      	println('enabled', 'disabling');
        plugin.disable();
      }
    }
  }
  
}

class PluginCheck{

	String key; 
	BigDecimal pluginVersion;
	def internalPlugin;

	PluginCheck(String artifactId, String version){

					key = artifactId;
		pluginVersion = new BigDecimal(version);
		internalPlugin = new ArrayList<String>(Jenkins.instance.pluginManager.plugins).find {x -> x.shortName == this.key};

	}
  	
  	Boolean getEnabled(){
  		return internalPlugin.isEnabled();
  	}
  
  	void disable(){
  		internalPlugin.disable();
  	}

	String shortName(){
		return internalPlugin.shortName;
	}

	String displayName(){
		return internalPlugin.displayName;
	}

	String installVersion(){
		return internalPlugin.version;
	}

	Boolean isInstalled(){
		return internalPlugin != null && new BigDecimal(internalPlugin.version) > this.pluginVersion;
	}

	void install(){
		Jenkins.instance.updateCenter.getPlugin(key).deploy();

	}
}


def pluginCheck = new PluginCheck("jobConfigHistory", "2.0");
def pluginManager = new PluginManager();

//println(pluginCheck.isInstalled() ? "Is Installed" : "Not Installed");

if (!pluginCheck.isInstalled()){
	pluginCheck.install();
}
else{
	println(pluginCheck.isInstalled() ? "Is Installed" : "Not Installed");
	println(pluginCheck.shortName());
	println(pluginCheck.installedVersion());
}



/*for (plugin in pluginManager.pluginIds){
	println(plugin);
}
for (plugin in pluginManager.installedPluginIds()){
	println('t' + plugin);
}*/

for (plugin in pluginManager.installedNotInWhitelist()){
	println(plugin);
}

for (plugin in pluginManager.(whitelistedNotInstalled()){
	println(plugin);
}

//pluginManager.disableNotWhitelisted();
pluginManager.installWhitelisted();
     
