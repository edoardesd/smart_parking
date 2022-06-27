package com.example.smartparking.internal

import java.io.IOException
import java.lang.Exception

class NoConnectivityException: IOException()
class NavigationDetailsNotFoundException: Exception()
class LocationPermissionNotGrantedException: Exception()