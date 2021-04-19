package com.think.runex.datasource.api

import retrofit2.HttpException
import java.lang.Exception
import java.lang.RuntimeException

class ApiException(override val message: String?) : RuntimeException(message)