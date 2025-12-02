package devgyu.koreAi.presentation.screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import devgyu.koreAi.domain.entity.FluxImage
import devgyu.koreAi.domain.entity.FluxImageRes
import devgyu.koreAi.domain.model.GeneratedImageIdWithPrompt
import devgyu.koreAi.presentation.GoogleAdsUtils
import devgyu.koreAi.presentation.IntentUtil
import devgyu.koreAi.presentation.LocalSnackBarMessage
import devgyu.koreAi.presentation.R
import devgyu.koreAi.presentation.annotation.DefaultPreview
import devgyu.koreAi.presentation.designsystem.Colors
import devgyu.koreAi.presentation.designsystem.Typography
import devgyu.koreAi.presentation.designsystem.component.LottieLoading
import devgyu.koreAi.presentation.designsystem.component.icon.StableIcon
import devgyu.koreAi.presentation.designsystem.component.image.StableAsyncImage
import devgyu.koreAi.presentation.designsystem.component.image.StableImage
import devgyu.koreAi.presentation.designsystem.component.modifier.bounceOnPressWithClickable
import devgyu.koreAi.presentation.designsystem.component.modifier.forceColorClickable
import devgyu.koreAi.presentation.designsystem.component.text.DevGyuTextField
import devgyu.koreAi.presentation.designsystem.component.text.ImmutableSizeText
import devgyu.koreAi.presentation.model.FluxImageStyle
import devgyu.koreAi.presentation.model.fluximage.ImageGuide
import devgyu.koreAi.presentation.viewmodel.base.HomeViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningFold

private val defaultPadding = 14.dp
const val PROMPT_MAX_LENGTH = 200
private const val PROMPT_CONTENT_TYPE = "PROMPT"
private const val IMAGE_STYLE_CONTENT_TYPE = "IMAGE_STYLE_CONTENT_TYPE"
private const val IMAGE_PROMPT_EXPORT_CONTENT_TYPE = "IMAGE_PROMPT_EXPORT_CONTENT_TYPE"
private const val IMAGE_GUIDE_CONTENT_TYPE = "IMAGE_GUIDE_CONTENT_TYPE"
private const val GENERATED_IMAGE_DATA_CONTENT_TYPE = "GENERATED_IMAGE_DATA_CONTENT_TYPE"

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val fluxImageModel by viewModel.fluxImageModel.collectAsStateWithLifecycle()
    val showPreviousPrompt by viewModel.showPreviousPrompt.collectAsStateWithLifecycle()
    val fluxImageRes by viewModel.fluxImageResponse.collectAsStateWithLifecycle()
    val imageStyleList = remember { FluxImageStyle.entries.toImmutableList() }
    val generatedImageIdWithPromptList by viewModel.generatedImageIdWithPromptList.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    BackHandler(fluxImageRes != null) {
        viewModel.removeFluxImageRes()
    }

    LaunchedEffect(true) {
        viewModel.adsChannelFlow
            .receiveAsFlow()
            .collectLatest {
                GoogleAdsUtils.showAds(context){ success ->
                    when {
                        success -> viewModel.callCreateImageApi() // 이미지 생성
                        else -> viewModel.updateSnackbarMessage(context.getString(R.string.loadingAdMessage))
                    }
                }

                GoogleAdsUtils.loadAds(context)
            }
    }

    HomeContent(
        prompt = fluxImageModel.prompt,
        imageStyleList = imageStyleList,
        generatedImageIdWithPromptList = generatedImageIdWithPromptList.toImmutableList(),
        selectedImageStyle = fluxImageModel.selectedImageStyle,
        updatePrompt = viewModel::updatePrompt,
        clearPrompt = viewModel::clearPrompt,
        requestFluxImage = viewModel::createFluxImage,
        showPreviousPrompt = showPreviousPrompt,
        onImageClick = viewModel::clickGuideImage,
        onShowPreviousPrompt = viewModel::onShowPreviousButtonClick,
        onImageStyleChoose = viewModel::updateImageStyle,
        deleteGeneratedImageData = viewModel::deleteGeneratedImageData
    )

    LottieLoading(isLoading)

    if(fluxImageRes != null){
        FluxImageComponent(
            fluxImageRes = fluxImageRes!!,
            removeFluxImageRes = viewModel::removeFluxImageRes,
            onClickSaveImage = viewModel::onClickSaveImage,
            onClickReportImage = { res -> IntentUtil.sendReportImageData(context, res) }
        )
    }

    LocalSnackBarMessage(
        snackbarChannel = viewModel.snackBarChannel,
        snackBarHostState = LocalSnackbarHostState.current
    )
}

/**
 * LazyVerticalStaggeredGrid 에서 contentPadding 사용하고
 * 텍스트 필드 입력 시도 후 스크롤 마구 하면 [java.lang.IllegalStateException: Place was called on a node which was placed already]
 * 발생 ..
 * 이유는 모름
 * */
@Composable
private fun HomeContent(
    prompt: String,
    selectedImageStyle: FluxImageStyle,
    showPreviousPrompt: Boolean,
    generatedImageIdWithPromptList: ImmutableList<GeneratedImageIdWithPrompt>,
    imageStyleList: ImmutableList<FluxImageStyle>,
    onImageStyleChoose: (FluxImageStyle) -> Unit,
    onImageClick: (String) -> Unit,
    requestFluxImage: () -> Unit,
    clearPrompt: () -> Unit,
    deleteGeneratedImageData: (Long) -> Unit,
    onShowPreviousPrompt: () -> Unit,
    updatePrompt: (String) -> Unit,
){
    val imageGuideList = remember {
        ImageGuide.imageGuideList().chunked(2) { it[0] to it[1] }}
    val lazyColumnState = rememberLazyListState()

    LaunchedEffect(prompt) {
        if(lazyColumnState.layoutInfo.visibleItemsInfo.none { it.key == PROMPT_CONTENT_TYPE }){
            lazyColumnState.animateScrollToItem(0)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = lazyColumnState,
            contentPadding = PaddingValues(vertical = 30.dp)
        ) {
            item(contentType = PROMPT_CONTENT_TYPE) {
                Column {
                    CreatePromptSection(
                        prompt = prompt,
                        generatedImageIdWithPromptList = generatedImageIdWithPromptList,
                        updatePrompt = updatePrompt,
                        clearPrompt = clearPrompt,
                        showPreviousPrompt = showPreviousPrompt,
                        onShowPreviousPrompt = onShowPreviousPrompt,
                        deleteGeneratedImageData = deleteGeneratedImageData
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                }
            }

            item(contentType = IMAGE_STYLE_CONTENT_TYPE) {
                ChooseImageStyleSection(
                    selectedImageStyle = selectedImageStyle,
                    imageStyleList = imageStyleList,
                    onImageStyleChoose = onImageStyleChoose
                )
                Spacer(modifier = Modifier.height(30.dp))
            }

            item(contentType = IMAGE_PROMPT_EXPORT_CONTENT_TYPE) {
                TitleAndSpacer(title = stringResource(R.string.extractImagePrompt))
            }

            items(
                items = imageGuideList,
                key = { guide -> guide.first.drawableRes },
                contentType = { IMAGE_GUIDE_CONTENT_TYPE }
            ){ guide ->
                val (first, second) = guide
                GuideImageSection(first, second, onImageClick)
            }
        }

        ImmutableSizeText(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = defaultPadding)
                .padding(bottom = 30.dp)
                .imePadding()
                .clip(CircleShape)
                .background(Colors.ButtonBlue, CircleShape)
                .forceColorClickable(onClickEvent = requestFluxImage)
                .padding(vertical = 12.dp, horizontal = 20.dp),
            text = stringResource(R.string.generateImage),
            color = Colors.White,
            style = Typography.TitleMB,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun CreatePromptSection(
    prompt: String,
    generatedImageIdWithPromptList: ImmutableList<GeneratedImageIdWithPrompt>,
    showPreviousPrompt: Boolean,
    updatePrompt: (String) -> Unit,
    clearPrompt: () -> Unit,
    deleteGeneratedImageData: (Long) -> Unit,
    onShowPreviousPrompt: () -> Unit,
){
    val lazyRowState = rememberLazyListState()
    val generatedImageDataSize by rememberUpdatedState(generatedImageIdWithPromptList.size)

    LaunchedEffect(true) {
        snapshotFlow { generatedImageDataSize }
            .runningFold(Pair(0, 0)) { acc, newSize ->
                Pair(acc.second, newSize)  // 이전 크기와 현재 크기 저장
            }
            .collectLatest { (previousSize, currentSize) ->
                if(currentSize > previousSize){
                    lazyRowState.animateScrollToItem(0)
                }
            }
    }

    Column(modifier= Modifier.fillMaxWidth()) {
        TitleAndSpacer(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(R.string.createYourOwnImage),
            style = Typography.TitleMB
        )

        Column(
            modifier = Modifier
                .padding(horizontal = defaultPadding)
                .fillMaxWidth()
                .background(Colors.Divider, shape = RoundedCornerShape(10.dp))
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DevGyuTextField(
                decorationBoxClipShape = RoundedCornerShape(0.dp),
                textBoxModifier = Modifier
                    .fillMaxWidth()
                    .height(103.dp)
                    .background(Color.Unspecified),
                textBoxAlignment = Alignment.TopStart,
                textStyle = Typography.TextMR,
                value = prompt,
                onValueChange = updatePrompt,
                keyboardOptions = KeyboardOptions().copy(imeAction = ImeAction.Done),
                placeholder = { color, style ->
                    ImmutableSizeText(
                        text = stringResource(R.string.inputImageText),
                        style = Typography.TextSR,
                        color = color
                    )
                }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ImmutableSizeText(
                    modifier = Modifier
                        .clip(CircleShape)
                        .forceColorClickable(onClickEvent = clearPrompt)
                        .padding(horizontal = 4.dp, vertical = 2.dp),
                    text = stringResource(R.string.clearText),
                    style = Typography.TextXSR,
                    color = Colors.GrayBlack
                )

                ImmutableSizeText(
                    text = "${prompt.length}/$PROMPT_MAX_LENGTH",
                    style = Typography.TextXSR,
                    color = Colors.LightBlack
                )
            }
        }

        AnimatedVisibility(showPreviousPrompt && generatedImageIdWithPromptList.isNotEmpty()) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp),
                state = lazyRowState,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(horizontal = defaultPadding)
            ) {
                items(
                    items = generatedImageIdWithPromptList,
                    key = { createdImageIdWithPrompt -> createdImageIdWithPrompt.id },
                    contentType = { GENERATED_IMAGE_DATA_CONTENT_TYPE }
                ){ createdImageIdWithPrompt ->
                    Row(
                        modifier = Modifier
                            .clip(CircleShape)
                            .border(
                                width = 1.dp,
                                color = Colors.Divider,
                                shape = CircleShape
                            )
                            .forceColorClickable {
                                updatePrompt(createdImageIdWithPrompt.prompt)
                            }
                            .padding(horizontal = 8.dp, vertical = 5.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ImmutableSizeText(
                            text = createdImageIdWithPrompt.prompt,
                            style = Typography.TextXSR,
                            color = Colors.Black,
                            maxLines = 1
                        )

                        StableIcon(
                            modifier = Modifier.forceColorClickable {
                                deleteGeneratedImageData(createdImageIdWithPrompt.id)
                            },
                            drawableRes = R.drawable.ic_close,
                            description = "삭제 이미지"
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))
        ImmutableSizeText(
            modifier = Modifier
                .padding(horizontal = defaultPadding)
                .fillMaxWidth()
                .bounceOnPressWithClickable(
                    bounceModifier = Modifier
                        .background(Colors.ButtonBlue, shape = CircleShape)
                        .clip(CircleShape),
                    onClickEvent = onShowPreviousPrompt,
                    bounceScale = 0.97f,
                    disableRepeatedClicks = false,
                )
                .padding(horizontal = 20.dp, vertical = 12.dp),
            textAlign = TextAlign.Center,
            style = Typography.TextSB,
            color = Colors.White,
            text = when {
                showPreviousPrompt -> stringResource(R.string.hidePreviousHistory)
                else -> stringResource(R.string.showPreviousHistory)
            }
        )
    }
}

@Composable
private fun ChooseImageStyleSection(
    selectedImageStyle: FluxImageStyle,
    imageStyleList: ImmutableList<FluxImageStyle>,
    onImageStyleChoose: (FluxImageStyle) -> Unit
){
    val lazyListState = rememberLazyListState()
    val context = LocalContext.current

    // 선택한 스타일이 가운데로 오게끔 설정해준다.
    LaunchedEffect(selectedImageStyle) {
        val styleIdx = imageStyleList.indexOf(selectedImageStyle)
        val itemInfo = lazyListState.layoutInfo.visibleItemsInfo.firstOrNull {
            it.index == styleIdx
        }

        when (itemInfo != null) {
            true -> {
                val center = lazyListState.layoutInfo.viewportEndOffset / 2
                val childCenter = itemInfo.offset + itemInfo.size / 2
                lazyListState.animateScrollBy((childCenter - center).toFloat())
            }

            else -> lazyListState.animateScrollToItem(styleIdx)
        }
    }

    Column(modifier= Modifier.fillMaxWidth()){
        TitleAndSpacer(title = stringResource(R.string.selectDefaultStyle))
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            state = lazyListState,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = defaultPadding)
        ){
            items(
                items = imageStyleList,
                key = { it.ordinal },
                contentType = { "ImageStyle" }
            ){ imageStyle ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StableImage(
                        modifier = Modifier
                            .bounceOnPressWithClickable(
                                bounceModifier = Modifier
                                    .width(120.dp)
                                    .height(95.dp)
                                    .then(
                                        when {
                                            selectedImageStyle == imageStyle ->
                                                Modifier
                                                    .alpha(1.0f)
                                                    .scale(1.0f)
                                                    .border(
                                                        2.dp,
                                                        Colors.SelectBorder,
                                                        RoundedCornerShape(10.dp)
                                                    )

                                            else -> Modifier
                                                .alpha(0.7f)
                                                .scale(0.93f)
                                        }
                                    )
                                    .clip(RoundedCornerShape(10.dp)),
                                disableRepeatedClicks = false,
                                onClickEvent = { onImageStyleChoose(imageStyle) }
                            ),
                        drawableRes = imageStyle.drawableRes,
                        contentScale = ContentScale.Crop,
                        description = "스타일 이미지"
                    )

                    ImmutableSizeText(
                        text = imageStyle.stringRes,
                        textAlign = TextAlign.Center,
                        style = Typography.TextXSB,
                        color = Colors.LightBlack
                    )
                }
            }
        }
    }
}

@Composable
private fun GuideImageSection(
    firstGuide: ImageGuide,
    secondGuide: ImageGuide,
    onImageClick: (String) -> Unit
){
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = defaultPadding)
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        StableImage(
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1.0f / 1.3f)
                .clip(RoundedCornerShape(6.dp))
                .forceColorClickable { onImageClick(context.getString(firstGuide.promptRes)) },
            drawableRes = firstGuide.drawableRes,
            contentScale = ContentScale.FillHeight,
            description = ""
        )

        StableImage(
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1.0f / 1.3f)
                .clip(RoundedCornerShape(6.dp))
                .forceColorClickable { onImageClick(context.getString(secondGuide.promptRes)) },
            drawableRes = secondGuide.drawableRes,
            contentScale = ContentScale.FillHeight,
            description = ""
        )
    }
}

@Composable
private fun TitleAndSpacer(
    modifier: Modifier = Modifier,
    title: String,
    style: TextStyle = Typography.TextMB,
){
    ImmutableSizeText(
        modifier = modifier
            .padding(bottom = 16.dp)
            .padding(horizontal = defaultPadding),
        text = title,
        style = style,
        color = Colors.LightBlack
    )
}

@Composable
private fun FluxImageComponent(
    fluxImageRes: FluxImageRes,
    removeFluxImageRes: () -> Unit,
    onClickSaveImage: (FluxImageRes) -> Unit,
    onClickReportImage: (FluxImageRes) -> Unit
){
    var isImageLoading by remember { mutableStateOf(false) }

    LottieLoading(
        text = stringResource(R.string.loadingImage),
        isLoading = isImageLoading
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Colors.LightBlack)
            .padding(top = 30.dp)
    ){
        StableIcon(
            modifier = Modifier
                .padding(start = defaultPadding)
                .clip(CircleShape)
                .forceColorClickable { removeFluxImageRes() },
            drawableRes = R.drawable.ic_back,
            description = "뒤로 가기"
        )

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.End
        ) {
            StableAsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(12.dp)),
                model = fluxImageRes.image.url,
                previewModel = R.drawable.img_japanse_anime,
                contentDescription = "생성 이미지",
                onLoading = { isImageLoading = true },
                onSuccess = { isImageLoading = false },
                onError = { isImageLoading = false }
            )

            Row(
                modifier = Modifier.padding(end = 20.dp, top = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ReportImage(fluxImageRes, onClickReportImage)
                DownloadImage(fluxImageRes, onClickSaveImage)
            }
        }
    }
}

@Composable
private fun ReportImage(
    fluxImageRes: FluxImageRes,
    onClickReportImage: (FluxImageRes) -> Unit
){
    Row(
        modifier = Modifier
            .background(Colors.ReportRed, RoundedCornerShape(30.dp))
            .clip(RoundedCornerShape(30.dp))
            .forceColorClickable { onClickReportImage(fluxImageRes) }
            .padding(horizontal = 10.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ImmutableSizeText(
            text = stringResource(R.string.reportImage),
            style = Typography.TextSM,
            color = Colors.White
        )
    }
}

@Composable
private fun DownloadImage(
    fluxImageRes: FluxImageRes,
    onClickSaveImage: (FluxImageRes) -> Unit
){
    Row(
        modifier = Modifier
            .background(Colors.ButtonBlue, RoundedCornerShape(30.dp))
            .clip(RoundedCornerShape(30.dp))
            .forceColorClickable { onClickSaveImage(fluxImageRes) }
            .padding(horizontal = 10.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        StableIcon(
            drawableRes = R.drawable.ic_download,
            description = "다운로드 이미지"
        )

        ImmutableSizeText(
            text = stringResource(R.string.saveImage),
            style = Typography.TextSM,
            color = Colors.White
        )
    }
}

@DefaultPreview
@Composable
private fun PreviewHomeContent(){
    HomeContent(
        prompt = "",
        requestFluxImage = {},
        clearPrompt = {},
        imageStyleList = emptyList<FluxImageStyle>().toImmutableList(),
        onShowPreviousPrompt = {},
        showPreviousPrompt = true,
        updatePrompt = {},
        onImageClick = {},
        selectedImageStyle = FluxImageStyle.None,
        onImageStyleChoose = {},
        deleteGeneratedImageData = {},
        generatedImageIdWithPromptList = emptyList<GeneratedImageIdWithPrompt>().toImmutableList()
    )
}

@DefaultPreview
@Composable
private fun PreviewFluxImageComponent(){
    FluxImageComponent(
        fluxImageRes = FluxImageRes(
            image = FluxImage("", ""),
            prompt = "",
            hasNSFWConcepts = false,
            seed = 1414
        ),
        removeFluxImageRes = {},
        onClickSaveImage = {},
        onClickReportImage = {}
    )
}