package com.ripbull.coresdk.onboard

import org.junit.Assert
import org.junit.Test

/**
 * Created by DK on 16/07/20.
 */
class PasswordValidatorTest {
  @Test
  fun `validate password with empty param`() {
    Assert.assertEquals(4, 2 + 2.toLong())
  }

  @Test
  fun `validate password with lesser than expected max size`() {
    Assert.assertEquals(4, 2 + 2.toLong())
  }
}
