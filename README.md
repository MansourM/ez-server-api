[![](https://jitpack.io/v/MansourM/ez-server-api.svg)](https://jitpack.io/#MansourM/ez-server-api)

# ez-server-api
this is an unfinished api for personal use

# Import:

Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

Step 2. Add the dependency

    dependencies {
	        implementation 'com.github.MansourM.ez-server-api:{$networkLibrary}:{$version}'
	}
	
examples

    implementation 'com.github.MansourM.ez-server-api:ion:0.1.5'
    implementation 'com.github.MansourM.ez-server-api:fan:0.1.4' //fan lib not implemented yet
