package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("أكاديمية رقيب", appName)
  }

  @Test
  fun `test educational solver complex number`() {
    val solution = com.example.solver.EducationalSolver.solve("ع = 1 + ت")
    assertEquals(false, solution.isExamSheet)
    org.junit.Assert.assertTrue(solution.finalAnswer.isNotEmpty())
  }

  @Test
  fun `test 4-stage owner credentials constants`() {
    assertEquals("785502919", com.example.data.AppDataRepository.OWNER_PHONE)
    assertEquals("abdulrqeebfarag2030@gmail.com", com.example.data.AppDataRepository.OWNER_EMAIL)
  }
}
