package com.iqbalwork.ramadhancamp.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.iqbalwork.ramadhancamp.ui.component.LocationLabel
import com.iqbalwork.ramadhancamp.ui.component.TextSectionLabel
import com.iqbalwork.ramadhancamp.ui.theme.ArabicTypography
import com.iqbalwork.ramadhancamp.ui.theme.BackgroundItem
import com.iqbalwork.ramadhancamp.ui.theme.PrimaryGreen
import com.iqbalwork.ramadhancamp.ui.theme.RamadhanTheme
import com.iqbalwork.ramadhancamp.ui.theme.RamadhanTypography
import com.iqbalwork.ramadhancamp.ui.theme.SecondaryGreen
import org.jetbrains.compose.resources.painterResource
import ramadhancamp.composeapp.generated.resources.Res
import ramadhancamp.composeapp.generated.resources.ic_book
import ramadhancamp.composeapp.generated.resources.img_mosque

/**
 * iqbalfauzi
 * Email: work.iqbalfauzi@gmail.com
 * Github: https://github.com/iqbalwork
 */
@Composable
fun HomeContent() {
    Scaffold(
        modifier = Modifier,
        containerColor = MaterialTheme.colorScheme.primary
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                HeaderItem(
                    modifier = Modifier
                        .padding(top = paddingValues.calculateTopPadding())
                        .fillMaxWidth(), location = "Bandung, Jawa Barat"
                )
            }
            item {
                PrayTimeSection(modifier = Modifier.fillMaxWidth())
            }
            item {
                SearchBarItem()
            }
            item {
                LastReadSection()
            }
            item {
                PopularSection(modifier = Modifier.fillMaxWidth())
            }
            item {
                Spacer(modifier = Modifier.height(paddingValues.calculateBottomPadding()))
            }
        }
    }
}

@Composable
fun PopularSection(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextSectionLabel(label = "Surat Populer")
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            surahSamples.forEach {
                SurahItem(modifier = Modifier.fillMaxWidth(), surahData = it)
            }
        }
    }
}

val surahSamples = listOf(
    SurahData(
        name = "Yasin",
        arabicName = "يٰسۤ",
        number = 36,
        totalAyah = 83,
        location = "Mekah",
        description = "Surat Yaasiin terdiri atas 83 ayat, termasuk golongan surat-surat Makkiyyah,  diturunkan sesudah surat Jin. Dinamai Yaasiin karena dimulai dengan huruf Yaasiin. Sebagaimana halnya arti huruf-huruf abjad yang terletak pada permulaan beberapa surat Al Quran, maka demikian pula arti Yaasiin yang terdapat pada ayat permulaan surat ini, yaitu Allah mengisyaratkan bahwa sesudah huruf tersebut akan dikemukakan hal-hal yang penting antara lain: Allah bersumpah dengan Al Quran bahwa Muhammad s.a.w. benar-benar seorang rasul yang diutus-Nya kepada kaum yang belum pernah diutus kepada mereka rasul-rasul."
    ),
    SurahData(
        name = "Al Mulk",
        arabicName = "الملك",
        number = 67,
        totalAyah = 30,
        location = "Mekah",
        description = "Surat ini terdiri atas 30 ayat, termasuk golongan surat-surat  Makkiyah, diturunkan sesudah Ath Thuur. Nama Al Mulk diambil dari kata Al Mulk yang terdapat pada ayat pertama surat ini yang artinya kerajaan atau kekuasaan. Dinamai pula surat ini dengan At Tabaarak (Maha Suci)."
    ),
    SurahData(
        name = "Al Kahf",
        arabicName = "الكهف",
        number = 18,
        totalAyah = 110,
        location = "Mekah",
        description = "Surat  ini terdiri atas 110 ayat, termasuk  golongan  surat-surat Makkiyyah. Dinamai Al-Kahfi artinya Gua dan Ashhabul Kahfi yang artinya Penghuni-Penghuni Gua. Kedua nama ini diambil dari cerita yang terdapat dalam surat ini pada ayat 9 sampai dengan 26, tentang beberapa orang pemuda yang tidur dalam gua bertahun-tahun lamanya. Selain cerita tersebut, terdapat pula beberapa buah cerita dalam surat ini, yang kesemuanya mengandung i'tibar dan pelajaran-pelajaran yang amat berguna bagi kehidupan manusia. Banyak hadist-hadist Rasulullah s.a.w. yang menyatakan keutamaan membaca surat ini."
    )
)

data class SurahData(
    val name: String,
    val arabicName: String,
    val number: Int,
    val totalAyah: Int,
    val location: String,
    val description: String,
)

@Composable
fun SurahItem(modifier: Modifier = Modifier, surahData: SurahData) {
    ConstraintLayout(
        modifier = modifier
            .background(color = BackgroundItem, shape = RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        val (numberRef, surahRef, arabicRef) = createRefs()

        Text(
            text = surahData.number.toString(),
            style = RamadhanTypography.bodyMedium,
            modifier = Modifier.constrainAs(numberRef) {
                linkTo(top = parent.top, bottom = parent.bottom)
                start.linkTo(parent.start)
            }
        )

        Column(modifier = Modifier.constrainAs(surahRef) {
            linkTo(top = parent.top, bottom = parent.bottom)
            start.linkTo(numberRef.end, 16.dp)
        }) {
            Text(
                text = surahData.name,
                style = RamadhanTypography.titleMedium
            )
            Text(
                text = "${surahData.totalAyah} Ayah • ${surahData.location}",
                style = RamadhanTypography.bodySmall
            )
        }

        Text(
            text = surahData.arabicName,
            style = ArabicTypography.titleLarge,
            modifier = Modifier.constrainAs(arabicRef) {
                linkTo(top = parent.top, bottom = parent.bottom)
                end.linkTo(parent.end)
            }
        )
    }
}

@Composable
fun PrayTimeSection(modifier: Modifier = Modifier) {
    val gradientColors = listOf(Color.Magenta, Color.Blue, Color.Cyan)

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = SecondaryGreen)
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (prayTimeRef, dateRef) = createRefs()
            Row(
                modifier = Modifier.constrainAs(prayTimeRef) {
                    top.linkTo(parent.top, 16.dp)
                    start.linkTo(parent.start, 16.dp)
                    end.linkTo(parent.end, 16.dp)
                    width = Dimension.fillToConstraints
                },
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(text = "Berikutnya", style = RamadhanTypography.bodySmall)
                    Text(text = "Maghrib", style = RamadhanTypography.titleLarge)
                    Text(text = "18:20", style = RamadhanTypography.titleMedium)
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(text = "Sisa Waktu", style = RamadhanTypography.bodySmall)
                    Text(
                        text = "45 Menit",
                        style = RamadhanTypography.titleLarge.copy(fontSize = 18.sp)
                    )
                }
            }

            Row(
                modifier = Modifier.background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(40.dp)
                ).padding(horizontal = 16.dp, vertical = 8.dp).constrainAs(dateRef) {
                    top.linkTo(prayTimeRef.bottom, 16.dp)
                    bottom.linkTo(parent.bottom, 16.dp)
                    start.linkTo(parent.start, 16.dp)
                },
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = null,
                    tint = Color.White,
                )
                Text(text = "Selasa, 24 Sya'ban 1447H", style = RamadhanTypography.bodySmall)
            }

            Image(
                painter = painterResource(Res.drawable.img_mosque),
                contentDescription = null,
                modifier = Modifier.alpha(0.25f).constrainAs(createRef()) {
                    bottom.linkTo(parent.bottom, (-16).dp)
                    end.linkTo(parent.end, (-16).dp)
                }
            )
        }
    }
}

@Composable
fun LastReadSection(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextSectionLabel(label = "Terakhir dibaca")
            Text(text = "Lihat semua", style = RamadhanTypography.bodySmall)
        }

        ConstraintLayout(
            modifier = Modifier.fillMaxWidth()
                .background(color = BackgroundItem, shape = RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            val (iconRef, infoRef) = createRefs()

            Image(
                painter = painterResource(Res.drawable.ic_book), contentDescription = null,
                modifier = Modifier.constrainAs(iconRef) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
            )

            Column(modifier = Modifier.constrainAs(infoRef) {
                linkTo(top = iconRef.top, bottom = iconRef.bottom)
                start.linkTo(iconRef.end, margin = 16.dp)
            }) {
                Text(text = "Terakhir dibaca", style = RamadhanTypography.titleMedium)
                Text(text = "Lihat semua", style = RamadhanTypography.bodySmall)
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                modifier = Modifier.constrainAs(createRef()) {
                    linkTo(top = parent.top, bottom = parent.bottom)
                    end.linkTo(parent.end)
                })
        }
    }
}

@Composable
fun SearchBarItem(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .background(color = BackgroundItem, shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 12.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = Color.White
        )
        Text(text = "Cari Ayat atau Surat...", style = RamadhanTypography.bodyMedium)
    }
}

@Composable
fun HeaderItem(modifier: Modifier = Modifier, location: String) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "Assalammu'alaikum!",
            style = RamadhanTypography.titleLarge
        )
        LocationLabel(modifier = Modifier, location = "Bandung, Jawa Barat")
    }
}

@Preview
@Composable
fun PreviewHomeContent() {
    RamadhanTheme(useDynamicColor = false) {
        HomeContent()
    }
}
