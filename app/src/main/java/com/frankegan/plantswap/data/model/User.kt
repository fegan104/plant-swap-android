package com.frankegan.plantswap.data.model

import java.lang.Exception

class NoUserFoundException: Exception("No user is signed in")

inline class UserId(val id: String)
