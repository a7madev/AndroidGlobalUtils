[ ![Download](https://api.bintray.com/packages/a7madev/maven/AndroidGlobalUtils/images/download.svg) ](https://bintray.com/a7madev/maven/AndroidGlobalUtils/_latestVersion)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-AndroidGlobalUtils-green.svg?style=true)](https://android-arsenal.com/details/1/2969)

AndroidGlobalUtils
===================

# Overview

Global Utilities for Android

# Installation

## Gradle

#### Step 1: Add to project build.gradle

    dependencies {
            compile 'me.a7madev.androidglobalutils:library:1.0.12'
    }

#### Step 2: Import package in your java class

    import me.a7madev.androidglobalutils.*;

# Usage

GlobalUtils Class
----------

:   isDebug
> Is build in debug config or release

:   logThis
> Log message and exception

:   showToast
> Make a standard toast that just contains a text view.

:   checkForInternetConnection
> Check if Internet is available and connected

:   getTypeFace
> Get an array of heterogeneous values.

:   getTypedArrayResource
> Create a new typeface from font file.

:   getStringArray
> Return the string array associated with a particular resource ID.

:   getIntArray
> Return the int array associated with a particular resource ID.

:   convertStringArrayToArrayList
> Convert String Array to Array List

:   getContentFromClipboard
> Get content from clipboard

:  requestMultiplePermissions
> Request Multiple Permissions (Android M+)

:  initProgressDialog
> Initialize Progress Dialog

:  dismissProgressDialog
> Dismiss Progress Dialog

:  getBitmapByResourceID
> Get Bitmap by Resource ID

:  getShareFileIntent
> Return share file intent

:  openShareFileIntent
> Opens share file intent

:  getShareTextIntent
> Get share text intent:  

: openShareTextIntent
> Open share text intent

:  getCallIntent
> Get call intent: 
 
 openCallIntent
> Open call intent

:  getURLIntent
> Get URL intent:  

:  openURLIntent
> Open URL intent

:  getnEmailIntent
> Get Email intent

:  openEmailIntent
> Open Email intent

:  changeActivityTheme
> Change Activity Theme

GlobalFileUtils Class
----------

:   getMimeType
> Get File Mime Type

:   openFileIntent
> Open File using intent (starts activity)

:   getFileIntent
> Return Intent to open any files

:   getFilesListFromDirectory
> Return list of files in a directory

:   getMediaThumbnailFromFile
> Get image or video thumbnail from a file

:   deleteFile
> Delete a file

:   getFileNameFromURL
> Get file name from url

:   getStorageDirectory
> Get Storage Directory, if does not exist, return download directory

:   isExternalStorageWritable
> Checks if external storage is available for read and write

:   isExternalStorageReadable
> Checks if external storage is available to at least read


# License
This plugin is available under the [GPL License, Version 3.0](http://www.gnu.org/licenses/gpl-3.0.en.html).

(c) All rights reserved A7madev