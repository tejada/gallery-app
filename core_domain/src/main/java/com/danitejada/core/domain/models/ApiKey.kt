package com.danitejada.core.domain.models

data class ApiKey(
  val value: String,
  val isValid: Boolean = true
)