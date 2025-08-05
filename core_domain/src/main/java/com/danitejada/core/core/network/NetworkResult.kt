package com.danitejada.core.core.network

sealed class NetworkResult<out T> {
  data class Success<T>(val data: T) : NetworkResult<T>()
  data class Error(val message: String) : NetworkResult<Nothing>()
  data object Loading : NetworkResult<Nothing>()
}

inline fun <T> NetworkResult<T>.onSuccess(action: (T) -> Unit): NetworkResult<T> {
  if (this is NetworkResult.Success) action(data)
  return this
}

inline fun <T> NetworkResult<T>.onError(action: (String) -> Unit): NetworkResult<T> {
  if (this is NetworkResult.Error) action(message)
  return this
}