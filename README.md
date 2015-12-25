AndroidGlobalUtils
===================

# Overview

Global Utilities for Android

# Installation

## Gradle

#### Step 1: Add to project build.gradle

    dependencies {
        compile 'me.a7madev.androidglobalutils:library:1.0.4'
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

GlobalFileUtils Class
----------

:   openFileIntent
> Open File using intent

:   getMimeType
> Get File Mime Type



# License
This plugin is available under the [GPL License, Version 3.0](http://www.gnu.org/licenses/gpl-3.0.en.html).

(c) All rights reserved A7madev