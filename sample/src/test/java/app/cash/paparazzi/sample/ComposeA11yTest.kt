package app.cash.paparazzi.sample

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.InstantAnimationsRule
import app.cash.paparazzi.Paparazzi
import app.cash.paparazzi.accessibility.AccessibilityRenderExtension
import com.google.testing.junit.testparameterinjector.TestParameter
import com.google.testing.junit.testparameterinjector.TestParameterInjector
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class ComposeA11yTest(
  @TestParameter renderAccessibilityInfo: Boolean,
) {
  @get:Rule
  val paparazzi = Paparazzi(
    deviceConfig = DeviceConfig.PIXEL.copy(
      screenWidth = DeviceConfig.PIXEL.screenWidth * (if (renderAccessibilityInfo) 2 else 1),
      softButtons = false,
    ),
    renderExtensions = if (renderAccessibilityInfo) setOf(AccessibilityRenderExtension()) else emptySet()
  )

  @get:Rule
  val instantAnimationsRule = InstantAnimationsRule()

  @Test
  fun compositeItems() {
    paparazzi.snapshot {
      Column {
        Row(
          Modifier
            .toggleable(
              value = true,
              role = Role.Checkbox,
              onValueChange = { }
            )
            .fillMaxWidth()
        ) {
          Text("Option", Modifier.weight(1f))
          Checkbox(checked = true, onCheckedChange = null)
        }
        Box(
          Modifier
            .align(Alignment.CenterHorizontally)
            .clickable(onClickLabel = "On Click Label") { }
        )
        Row(modifier = Modifier.semantics(mergeDescendants = true) {}) {
          Image(
            imageVector = Icons.Filled.Add,
            contentDescription = null // decorative
          )
          Column(modifier = Modifier.semantics(mergeDescendants = true) {}) {
            Text("Text")
            Text(
              text = "more text",
              modifier = Modifier.semantics { contentDescription = "custom description" }
            )
            Column(modifier = Modifier.semantics(mergeDescendants = true) {}) {
              Text("Nested text")
              Text(
                text = "more text",
                modifier = Modifier.semantics { contentDescription = "custom description" }
              )
            }
          }
        }
      }
    }
  }

  @Test
  fun animateContentSize() {
    paparazzi.snapshot {
      Text(
        text = "animateContentSize modifier is used",
        modifier = Modifier
          .fillMaxSize()
          .animateContentSize()
          .background(Color.LightGray),
        textAlign = TextAlign.End,
      )
    }
  }
}
