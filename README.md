# ez-server-api
this is an WIP api, developed to make specific REST calls easy and fast to code.

# Import:

### Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

### Step 2. Add the dependency

    dependencies {
	        implementation 'com.github.MansourM.ez-server-api:{$networkLibrary}:{$version}'
	}
	
##### Networking Libraries
"fan" uses [Fast Android Networking](https://github.com/amitshekhariitbhu/Fast-Android-Networking "Fast Android Networking") for networking

"ion" uses[ ION](https://github.com/koush/ion " ION") for networking (more utility but needs google play service to work)
	
##### Current Version 
[![](https://jitpack.io/v/MansourM/ez-server-api.svg)](https://jitpack.io/#MansourM/ez-server-api)
	
#### examples

    implementation 'com.github.MansourM.ez-server-api:fan:0.1.4'
    implementation 'com.github.MansourM.ez-server-api:ion:0.1.5'
    

