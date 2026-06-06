package com.nyra.app.android.feature.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nyra.app.android.R
import com.nyra.app.android.core.designsystem.theme.*
import com.nyra.app.android.core.model.CardAction
import com.nyra.app.android.core.model.CardActionType
import com.nyra.app.android.core.model.CardType
import com.nyra.app.android.core.model.PresenceCode
import com.nyra.app.android.feature.home.model.AtmosphereState
import com.nyra.app.android.feature.home.model.HomeCardUiModel
import com.nyra.app.android.feature.home.model.HomeCardVisualState
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun HomeMockup(
    modifier: Modifier = Modifier,
    atmosphere: AtmosphereState = AtmosphereState.EVENING,
    presenceCode: PresenceCode = PresenceCode.NeutralPresence,
    cards: List<HomeCardUiModel> = emptyList(),
    greeting: String = stringResource(R.string.greeting_evening),
    subtitle: String = stringResource(R.string.greeting_subtitle),
    onCardClick: (CardAction) -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(atmosphere.gradientColors.last())
    ) {
        // Layer 1: Background Gradient
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(atmosphere.gradientColors))
        )

        // Layer 2: Ambient Blobs
        AtmosphericBlobs(atmosphere)

        // Layer 3: Content
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                HomeContent(presenceCode, cards, greeting, subtitle, atmosphere, onCardClick)
            }
            HomeBottomNavigation()
        }
    }
}

@Composable
private fun AtmosphericBlobs(atmosphere: AtmosphereState) {
    val infiniteTransition = rememberInfiniteTransition(label = "blobs")
    
    val blobOffset by infiniteTransition.animateValue(
        initialValue = Offset(-100f, -100f),
        targetValue = Offset(100f, 100f),
        typeConverter = Offset.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = atmosphere.motionSpeed * 2, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "blob"
    )

    val glowColor = when (atmosphere) {
        AtmosphereState.EVENING -> GemPurpleGlow
        AtmosphereState.MORNING -> GemBlueGlow
        AtmosphereState.NIGHT -> GemBlueGlow
        AtmosphereState.AFTERNOON -> GemGreenGlow
        AtmosphereState.TIRED -> GemPeachGlow
        AtmosphereState.RESTLESS -> GemPinkGlow
        else -> HighlightWhite
    }

    Canvas(modifier = Modifier.fillMaxSize().alpha(atmosphere.glowIntensity * 0.4f)) {
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(glowColor.copy(alpha = 0.15f), Color.Transparent),
                center = Offset(size.width * 0.8f, size.height * 0.2f) + blobOffset,
                radius = size.width * 0.8f
            )
        )
    }
}

@Composable
private fun HomeContent(
    presenceCode: PresenceCode,
    cards: List<HomeCardUiModel>,
    greeting: String,
    subtitle: String,
    atmosphere: AtmosphereState,
    onCardClick: (CardAction) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = NyraSpacing.lg,
            end = NyraSpacing.lg,
            top = 64.dp,
            bottom = NyraSpacing.xl
        ),
        verticalArrangement = Arrangement.spacedBy(NyraSpacing.md)
    ) {
        item {
            HomeHeader(greeting, subtitle, atmosphere)
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(340.dp),
                contentAlignment = Alignment.Center
            ) {
                DynamicPresenceView(presenceCode = presenceCode, atmosphere = atmosphere)
            }
        }

        items(cards, key = { it.id }) { card ->
            NyraHomeCard(
                card = card,
                atmosphere = atmosphere,
                onClick = onCardClick
            )
        }

        item {
            Spacer(modifier = Modifier.height(NyraSpacing.sm))
            QuietActionCard(presenceCode = presenceCode, atmosphere = atmosphere, onClick = onCardClick)
        }
    }
}

@Composable
fun DynamicPresenceView(
    presenceCode: PresenceCode,
    atmosphere: AtmosphereState,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "presence")
    
    val breathingScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = atmosphere.motionSpeed, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathing"
    )

    val (glowColor, imageRes) = when (presenceCode) {
        PresenceCode.OpenPresence -> GemAmberStrong to R.drawable.red_1
        PresenceCode.CalmPresence, PresenceCode.QuietPresence -> GemBlueGlow to R.drawable.blue_1
        PresenceCode.GroundedPresence, PresenceCode.DrainedPresence -> GemGreenGlow to R.drawable.green_1
        else -> GemAmberStrong to R.drawable.red_3
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(320.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-35).dp)
                .width(170.dp)
                .height(40.dp)
                .blur(20.dp)
                .background(ShadowStrong.copy(alpha = 0.5f), CircleShape)
        )

        Box(
            modifier = Modifier
                .size(240.dp)
                .blur(35.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            glowColor.copy(alpha = 0.75f),
                            glowColor.copy(alpha = 0.25f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        Box(
            modifier = Modifier
                .size(120.dp)
                .offset(y = (-40).dp)
                .blur(25.dp)
                .background(
                    Brush.radialGradient(
                        listOf(HighlightWhite.copy(alpha = 0.3f), Color.Transparent)
                    ),
                    CircleShape
                )
        )

        Image(
            painter = painterResource(imageRes),
            contentDescription = null,
            modifier = Modifier
                .size(200.dp * breathingScale),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun NyraHomeCard(
    card: HomeCardUiModel,
    atmosphere: AtmosphereState,
    modifier: Modifier = Modifier,
    onClick: (CardAction) -> Unit = {}
) {
    val isHighlighted = card.visualState == HomeCardVisualState.Highlighted
    
    val cardBg = when (atmosphere) {
        AtmosphereState.EVENING -> CardEvening
        AtmosphereState.MORNING -> CardMorning
        AtmosphereState.NIGHT -> CardNight
        AtmosphereState.AFTERNOON -> CardAfternoon
        else -> CardQuiet
    }

    NyraCard(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (isHighlighted) Modifier.border(1.dp, CardStrokeLight, RoundedCornerShape(28.dp))
                else Modifier
            )
            .clickable { onClick(card.action) },
        contentPadding = PaddingValues(NyraSpacing.md)
    ) {
        Box(modifier = Modifier.matchParentSize().background(cardBg).alpha(0.5f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(CardWhiteOverlay)
                    .border(1.dp, CardStrokeLight, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (card.icon != null) {
                    Icon(
                        imageVector = card.icon,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = TextPrimary
                    )
                }
            }
            Spacer(modifier = Modifier.width(NyraSpacing.md))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = card.title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 18.sp,
                        fontWeight = if (isHighlighted) FontWeight.Medium else FontWeight.Normal
                    ),
                    color = TextPrimary.copy(alpha = atmosphere.textContrast)
                )
                card.subtitle?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary.copy(alpha = atmosphere.textContrast),
                        lineHeight = 18.sp
                    )
                }
            }
            Box(
                modifier = Modifier.size(70.dp, 40.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                CardAmbientDecoration(card.type)
                Icon(
                    modifier = Modifier.size(18.dp),
                    imageVector = arrowForwardIcon(),
                    contentDescription = null,
                    tint = TextSecondary.copy(alpha = 0.5f)
                )
            }
        }
    }
}

@Composable
private fun HomeHeader(greeting: String, subtitle: String, atmosphere: AtmosphereState) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column {
            Text(
                text = greeting,
                style = MaterialTheme.typography.displayMedium,
                color = TextPrimary.copy(alpha = atmosphere.textContrast)
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.headlineMedium,
                color = TextSecondary.copy(alpha = atmosphere.textContrast)
            )
        }
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(ButtonCircle)
                .border(1.dp, CardStrokeLight, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(24.dp).alpha(0.7f),
                imageVector = personIcon(),
                contentDescription = null,
                tint = TextPrimary
            )
        }
    }
}

@Composable
private fun QuietActionCard(presenceCode: PresenceCode, atmosphere: AtmosphereState, onClick: (CardAction) -> Unit) {
    NyraCard(
        modifier = Modifier.fillMaxWidth().clickable { onClick(CardAction(CardActionType.OpenPause)) },
        contentPadding = PaddingValues(horizontal = NyraSpacing.lg, vertical = NyraSpacing.xl)
    ) {
        Box(modifier = Modifier.matchParentSize().background(CardWhiteOverlay).alpha(0.2f))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(100.dp),
                contentAlignment = Alignment.Center
            ) {
                DynamicPresenceView(
                    presenceCode = presenceCode,
                    atmosphere = AtmosphereState.TIRED,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.width(NyraSpacing.lg))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.home_card_here_title),
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 28.sp),
                    color = TextPrimary.copy(alpha = atmosphere.textContrast)
                )
                Text(
                    text = stringResource(R.string.home_card_here_subtitle),
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                    color = TextSecondary.copy(alpha = atmosphere.textContrast)
                )
            }
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(ButtonCircle),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = arrowForwardIcon(),
                    contentDescription = null,
                    tint = TextPrimary
                )
            }
        }
    }
}

@Composable
private fun HomeBottomNavigation() {
    Surface(
        color = BottomBarDark,
        modifier = Modifier.fillMaxWidth().border(1.dp, BottomBarLightStroke, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = NyraSpacing.md, horizontal = NyraSpacing.md),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(icon = { HomeIcon(selected = true) }, label = stringResource(R.string.nav_home), selected = true)
            BottomNavItem(icon = { JournalIcon() }, label = stringResource(R.string.nav_journal))
            BottomNavItem(icon = { InsightsIcon() }, label = stringResource(R.string.nav_insights))
            BottomNavItem(icon = { SupportIcon() }, label = stringResource(R.string.nav_support))
        }
    }
}

@Composable
private fun BottomNavItem(
    icon: @Composable () -> Unit,
    label: String,
    selected: Boolean = false
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(NyraSpacing.xxs)
    ) {
        Box(
            modifier = Modifier
                .size(64.dp, 32.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(if (selected) ButtonCircleActive else Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = if (selected) ActiveIconAmber else InactiveIcon
        )
    }
}

@Composable
private fun CardAmbientDecoration(type: CardType) {
    when (type) {
        CardType.CheckIn -> WaveGraphic(color = GemAmberSoft)
        CardType.Reflection -> SunGraphic()
        CardType.GentleAction -> LeafGraphic()
        CardType.Breathing -> BreatheGraphic()
        else -> {}
    }
}

@Composable
private fun HomeIcon(selected: Boolean) {
    Canvas(modifier = Modifier.size(20.dp)) {
        val path = Path().apply {
            moveTo(size.width * 0.2f, size.height * 0.9f)
            lineTo(size.width * 0.2f, size.height * 0.45f)
            lineTo(size.width * 0.5f, size.height * 0.2f)
            lineTo(size.width * 0.8f, size.height * 0.45f)
            lineTo(size.width * 0.8f, size.height * 0.9f)
            close()
        }
        if (selected) drawPath(path = path, color = ActiveIconAmber)
        else drawPath(path = path, color = InactiveIcon, style = Stroke(width = 1.5.dp.toPx()))
    }
}

@Composable
private fun JournalIcon() {
    Canvas(modifier = Modifier.size(20.dp)) {
        drawRect(color = InactiveIcon, style = Stroke(width = 1.5.dp.toPx()))
        drawLine(color = InactiveIcon, start = Offset(size.width * 0.3f, size.height * 0.4f), end = Offset(size.width * 0.7f, size.height * 0.4f), strokeWidth = 1.5.dp.toPx())
    }
}

@Composable
private fun InsightsIcon() {
    Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
        Box(modifier = Modifier.size(3.dp, 8.dp).background(InactiveIcon, RoundedCornerShape(1.dp)))
        Box(modifier = Modifier.size(3.dp, 16.dp).background(InactiveIcon, RoundedCornerShape(1.dp)))
        Box(modifier = Modifier.size(3.dp, 12.dp).background(InactiveIcon, RoundedCornerShape(1.dp)))
    }
}

@Composable
private fun SupportIcon() {
    Canvas(modifier = Modifier.size(20.dp)) {
        drawCircle(color = InactiveIcon, style = Stroke(width = 1.5.dp.toPx()))
        drawCircle(color = InactiveIcon, radius = size.minDimension * 0.15f, style = Stroke(width = 1.5.dp.toPx()))
    }
}

@Composable
private fun WaveGraphic(color: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val path = Path().apply {
            moveTo(0f, size.height * 0.7f)
            quadraticTo(size.width * 0.25f, size.height * 0.5f, size.width * 0.5f, size.height * 0.7f)
            quadraticTo(size.width * 0.75f, size.height * 0.9f, size.width, size.height * 0.7f)
        }
        drawPath(path = path, color = color, style = Stroke(width = 1.dp.toPx()))
    }
}

@Composable
private fun SunGraphic() {
    Box(modifier = Modifier.size(40.dp), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(20.dp)) {
            drawCircle(brush = Brush.radialGradient(colors = listOf(GemAmberSoft, Color.Transparent)))
        }
    }
}

@Composable
private fun LeafGraphic() {
    Canvas(modifier = Modifier.size(30.dp, 40.dp)) {
        val path = Path().apply {
            moveTo(size.width * 0.5f, size.height)
            lineTo(size.width * 0.5f, size.height * 0.3f)
        }
        drawPath(path = path, color = GemGreenGlow.copy(alpha = 0.4f), style = Stroke(width = 1.2.dp.toPx()))
    }
}

@Composable
private fun BreatheGraphic() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val path = Path().apply {
            moveTo(0f, size.height * 0.5f)
            cubicTo(size.width * 0.3f, size.height * 0.3f, size.width * 0.7f, size.height * 0.7f, size.width, size.height * 0.5f)
        }
        drawPath(path = path, color = HighlightCool.copy(alpha = 0.3f), style = Stroke(width = 1.dp.toPx()))
    }
}

private fun personIcon() = ImageVector.Builder(
    name = "person", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f
).apply {
    path(fill = SolidColor(Color.White)) {
        moveTo(12f, 12f)
        curveTo(14.21f, 12f, 16f, 10.21f, 16f, 8f)
        curveTo(16f, 5.79f, 14.21f, 4f, 12f, 4f)
        curveTo(9.79f, 4f, 8f, 5.79f, 8f, 8f)
        curveTo(8f, 10.21f, 9.79f, 12f, 12f, 12f)
        close()
        moveTo(12f, 14f)
        curveTo(9.33f, 14f, 4f, 15.33f, 4f, 18f)
        verticalLineTo(20f)
        horizontalLineTo(20f)
        verticalLineTo(18f)
        curveTo(20f, 15.33f, 14.67f, 14f, 12f, 14f)
        close()
    }
}.build()

private fun arrowForwardIcon() = ImageVector.Builder(
    name = "arrow_forward", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f
).apply {
    path(fill = SolidColor(Color.White)) {
        moveTo(8.59f, 16.59f)
        lineTo(13.17f, 12f)
        lineTo(8.59f, 7.41f)
        lineTo(10f, 6f)
        lineTo(16f, 12f)
        lineTo(10f, 18f)
        close()
    }
}.build()

@Preview(showBackground = true)
@Composable
fun HomeMockupEveningTiredPreview() {
    NyraTheme {
        HomeMockup(
            atmosphere = AtmosphereState.TIRED,
            presenceCode = PresenceCode.DrainedPresence
        )
    }
}
