package com.github.snuffix.koleo.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.github.snuffix.koleo.R
import com.github.snuffix.koleo.secondary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

val stepFinishedColor = secondary
val stepNotStartedColor = Color(0x886e6e7d)

data class CalculationStep(
    val title: String = "",
    val stateText: String = ""
)

@Composable
fun ProcessWidget(
    modifier: Modifier,
    steps: List<CalculationStep>,
    titleVisibility: Boolean,
    subtitleVisibility: Boolean,
    currentStep: Int = 0,
    distanceInKilometers: String?,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        val iconSize = with(LocalDensity.current) {
            with(16.dp.toPx()) {
                Size(this, this)
            }
        }

        val iconAnimations = steps.map {
            remember { androidx.compose.animation.core.Animatable(0f) }
        }
        val backgroundPathsAnimations = steps.map {
            remember { androidx.compose.animation.core.Animatable(0f) }
        }
        val pathAnimations = steps.map {
            remember { androidx.compose.animation.core.Animatable(0f) }
        }

        val vectorIcons = steps.map { step ->
            rememberVectorPainter(image = ImageVector.vectorResource(id = R.drawable.ic_train))
        }

        LaunchedEffect(steps) {
            iconAnimations[0].animateTo(1f)
            iconAnimations[1].animateTo(1f)
            iconAnimations[2].animateTo(1f)

            backgroundPathsAnimations.forEachIndexed { index, animation ->
                launch {
                    delay(100 * index.toLong())
                    animation.animateTo(1f, animationSpec = tween(200))
                }
            }
        }

        LaunchedEffect(currentStep) {
            pathAnimations.subList(0, currentStep).forEachIndexed { index, animation ->
                launch {
                    delay(1000)
                    animation.animateTo(1f, animationSpec = tween(200))
                }.join()
            }
        }

        Box(
            modifier = Modifier
                .align(alignment = Alignment.Center)
                .padding(20.dp)
                .fillMaxWidth()
                .height(400.dp)
                .drawBehind {
                    val radius = minOf(size.width, size.height) / 2

                    val dashesCount = 150f
                    val dashPortion = 0.5f
                    val gapPortion = 0.5f
                    val circumference = 2f * Math.PI * size.width / 2f
                    val dashPlusGapSize = (circumference / dashesCount).toFloat()

                    val topLeft = Offset(size.width - radius * 2, size.height - radius * 2)

                    val stepAngle = 360 / steps.size
                    val stepAngles = List(steps.size) { (it + 1) * stepAngle }
                    val paddingAngle = 10f

                    steps.forEachIndexed { index, step ->
                        val angle = stepAngles[index]

                        val startAngle = 270f + angle - stepAngle + paddingAngle
                        val sweepAngle = stepAngle - paddingAngle * 2

                        val strokeWidth = 8f
                        val (color, stroke) = if (index <= currentStep) {
                            stepFinishedColor to Stroke(
                                width = strokeWidth,
                                cap = Stroke.DefaultCap,
                                join = StrokeJoin.Round
                            )
                        } else {
                            stepNotStartedColor to Stroke(
                                width = strokeWidth,
                                cap = Stroke.DefaultCap,
                                pathEffect = PathEffect.dashPathEffect(
                                    floatArrayOf(
                                        dashPlusGapSize * dashPortion,
                                        dashPlusGapSize * gapPortion
                                    ), 0f
                                )
                            )
                        }

                        drawArc(
                            startAngle = startAngle,
                            sweepAngle = sweepAngle * backgroundPathsAnimations[index].value,
                            color = color,
                            useCenter = false,
                            topLeft = topLeft,
                            size = Size(radius * 2, radius * 2),
                            style = stroke
                        )

                        drawArc(
                            startAngle = startAngle,
                            sweepAngle = sweepAngle * pathAnimations[index].value,
                            color = color,
                            useCenter = false,
                            topLeft = topLeft,
                            size = Size(radius * 2, radius * 2),
                            style = stroke
                        )


                        val centerCircleX = topLeft.x + radius
                        val centerCircleY = topLeft.y + radius

                        val circleStartAngle = 180 - index * stepAngle.toDouble()
                        val circleX = centerCircleX + radius * sin(Math.toRadians(circleStartAngle))
                        val circleY = centerCircleY + radius * cos(Math.toRadians(circleStartAngle))

                        drawCircle(
                            color = secondary,
                            radius = 36f * iconAnimations[index].value,
                            center = Offset(circleX.toFloat(), circleY.toFloat()),
                            style = Stroke(
                                width = 6f,
                            )
                        )

                        vectorIcons[index].let { painter ->
                            translate(
                                circleX.toFloat() - iconSize.width / 2,
                                circleY.toFloat() - iconSize.height / 2
                            ) {
                                if (iconAnimations[index].value == 1f) {
                                    with(painter) {
                                        draw(
                                            size = iconSize,
                                            colorFilter = ColorFilter.tint(secondary)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .align(alignment = Alignment.Center)
                    .wrapContentWidth()
            ) {
                val (title, subtitle) = createRefs()

                steps.getOrNull(currentStep)?.let { step ->
                    AnimatedVisibility(
                        modifier = Modifier.constrainAs(title) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                        visible = titleVisibility,
                        enter = fadeIn(
                            animationSpec = tween(
                                durationMillis = 500
                            )
                        )
                    ) {
                        Text(
                            text = step.title,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 60.sp,
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Light,
                            letterSpacing = 2.sp
                        )
                    }
                    AnimatedVisibility(
                        modifier = Modifier.constrainAs(subtitle) {
                            top.linkTo(title.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                        visible = subtitleVisibility,
                        enter = fadeIn(
                            animationSpec = tween(
                                durationMillis = 500
                            )
                        ) + slideInVertically(
                            animationSpec = tween(
                                durationMillis = 500
                            )
                        )
                    ) {
                        Text(
                            text = if (currentStep == steps.size - 1) {
                                "Distance: $distanceInKilometers"
                            } else {
                                step.stateText
                            },
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.secondary,
                            fontSize = 24.sp,
                            fontFamily = FontFamily.SansSerif
                        )

                    }
                }
            }
        }
    }
}
