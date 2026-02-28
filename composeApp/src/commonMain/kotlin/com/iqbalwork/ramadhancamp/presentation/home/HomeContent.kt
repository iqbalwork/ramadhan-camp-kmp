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
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import org.jetbrains.compose.resources.painterResource
import ramadhancamp.composeapp.generated.resources.Res
import ramadhancamp.composeapp.generated.resources.ic_book

/**
 * iqbalfauzi
 * Email: work.iqbalfauzi@gmail.com
 * Github: https://github.com/iqbalwork
 */
@Composable
fun HomeContent() {
    Scaffold(
        modifier = Modifier.background(color = Color.LightGray)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                HeaderItem(modifier = Modifier
                    .padding(top = paddingValues.calculateTopPadding())
                    .fillMaxWidth(), location = "Bandung, Jawa Barat")
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
        Text(text = "Surat Populer")
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
            .background(color = Color.DarkGray, shape = RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        val (numberRef, surahRef, arabicRef) = createRefs()

        Text(
            text = surahData.number.toString(),
            fontSize = 14.sp,
            color = Color.White,
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
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "${surahData.totalAyah} Ayah • ${surahData.location}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                color = Color.White
            )
        }

        Text(
            text = surahData.arabicName,
            fontSize = 28.sp,
            color = Color.White,
            modifier = Modifier.constrainAs(arabicRef) {
                linkTo(top = parent.top, bottom = parent.bottom)
                end.linkTo(parent.end)
            }
        )
    }
}

@Composable
fun PrayTimeSection(modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(text = "Berikutnya", fontSize = 14.sp)
                    Text(text = "Maghrib", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(text = "18:20", fontSize = 16.sp)
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(text = "Sisa Waktu", fontSize = 14.sp)
                    Text(text = "45 Menit", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.background(
                    color = Color.LightGray,
                    shape = RoundedCornerShape(40.dp)
                ).padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = null,
                    tint = Color.Blue,
                )
                Text(text = "Selasa, 24 Sya'ban 1447H", fontSize = 12.sp)
            }
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
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Terakhir dibaca")
            Text(text = "Lihat semua", fontSize = 12.sp, fontWeight = FontWeight.Light)
        }

        ConstraintLayout(
            modifier = Modifier.fillMaxWidth()
                .background(color = Color.LightGray, shape = RoundedCornerShape(12.dp))
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
                Text(text = "Terakhir dibaca")
                Text(text = "Lihat semua", fontSize = 12.sp, fontWeight = FontWeight.Light)
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
            .background(color = Color.LightGray, shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 12.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = Color.Blue
        )
        Text(text = "Cari Ayat atau Surat...")
    }
}

@Composable
fun HeaderItem(modifier: Modifier = Modifier, location: String) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Assalammu'alaikum!")
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = Color.Blue
            )
            Text(text = location)
        }
    }
}

@Preview
@Composable
fun PreviewHomeContent() {
    MaterialTheme {
        HomeContent()
    }
}
