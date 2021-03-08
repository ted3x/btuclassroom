package ge.ted3x.buildsrc

object Libraries {
    const val kotlinLib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
    const val coreKtxLib = "androidx.core:core-ktx:${Versions.ktx}"
    const val constraintLib = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"

    const val coroutinesLib = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"

    const val lifecycleLib = "androidx.lifecycle:lifecycle-common-java8:${Versions.lifecycleCommon}"
    const val lifecycleLiveDataLib = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycleLiveData}"
    const val lifecycleExtensionsLib = "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycleExtensions}"

    const val koinLib = "org.koin:koin-androidx-scope:${Versions.koin}"
    const val koinViewModelLib = "org.koin:koin-androidx-viewmodel:${Versions.koin}"
    const val koinFragmentLib = "org.koin:koin-androidx-fragment:${Versions.koin}"

    const val roomLib = "androidx.room:room-runtime:${Versions.roomDatabase}"
    const val roomCompiler = "androidx.room:room-compiler:${Versions.roomDatabase}"
    const val roomCoroutinesLib = "androidx.room:room-ktx:${Versions.roomDatabase}"

    const val ciceroneLib = "com.github.terrakok:cicerone:${Versions.cicerone}"

    const val circleImageViewLib = "de.hdodenhof:circleimageview:${Versions.circleImageView}"

    const val jsoupLib = "org.jsoup:jsoup:${Versions.jsoup}"

    const val junit = "junit:junit:${Versions.junit}"
    const val junitTest = "androidx.test.ext:junit:${Versions.junitTest}"
    const val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    const val testRunner = "androidx.test.runner:${Versions.testRunner}"
}