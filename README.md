# OtpView
Android CustomOtpView

Very Simple OtpView and it will support different otp size and it will support all the android version.

[![](https://www.jitpack.io/v/manishkummar21/otpview.svg)](https://www.jitpack.io/#manishkummar21/otpview)

# Installation
Step 1. Add it in your root build.gradle at the end of repositories:
```
allprojects {
		repositories {
			...
			maven { url 'https://www.jitpack.io' }
		}
	}
```  
Step 2. Add the dependency
```
dependencies {
	        implementation 'com.github.manishkummar21:otpview:1.0'
	}
 ``` 
 
Step 3. Include the View in XML

 ```

<com.otpview.OtpView
        android:layout_width="match_parent"
        android:layout_height="65dp"
        android:inputType="number"
        android:maxLength="6" />
        
  ```
  
  

 Note : You Can Change the length of Chars by setting attribute in maxlength.
