# Android Awesome RatingBar

<p align="center">
  <img src="/img/rtl.jpg" width="350" height="758" title="RTL Mode">
  <img src="/img/ltr.jpg" width="350" height="758" title="LTR Mode">
</p>

## [DEMO Video](/img/demo.mp4)

## Usage version : [![](https://jitpack.io/v/farsroidx/RatingBar.svg)](https://jitpack.io/#farsroidx/RatingBar)

### Gradle
```gradle
allprojects {
	repositories {
		maven { url 'https://jitpack.io' }
	}
}
```
```gradle
dependencies {
	implementation 'com.github.farsroidx:RatingBar:{version}'
}
```

### Maven
```maven
<repositories>
	<repository>
		<id>jitpack.io</id>
		<url>https://jitpack.io</url>
	</repository>
</repositories>
```
```maven
<dependency>
	<groupId>com.github.farsroidx</groupId>
	<artifactId>RatingBar</artifactId>
	<version>{version}</version>
</dependency>
```

## How to Use? | چگونه استفاده کنیم؟
### add this code in your xml activity view | اکتیویتی خود قرار دهید xml کد زیر را در فایل 
```xml
<ir.farsroidx.RatingView
   android:id="@+id/ratingView"
   android:layout_width="match_parent"
   android:layout_height="wrap_content"
   app:ratingBarMode="rtl"
   app:ratingReviewText="# Reviewed!"
   />
```
### app:ratingBarMode (enum)
## rtl , ltr | راست چین، چپ چین
```xml
<ir.farsroidx.RatingView
   app:ratingBarMode="rtl"
   />
```
### app:ratingReviewText (string)
## The number of points is replaced by # | تعداد رتبه ها با # جایگزین میشود
```xml
<ir.farsroidx.RatingView
   app:ratingReviewText="# Reviewed!"
   />
```



### Kotlin
```kotlin
// Review.STAR_5, Review.STAR_4, Review.STAR_3, Review.STAR_2 Review.STAR_1
ratingView.addReview(Review.STAR_5)

// Only Support of 1-5 number | فقط از اعداد 1 تا 5 پشتیبانی میکند
val values = listOf(5,3,2)
ratingView.addReview(values)

val allReviewsCount: Float = ratingView.getAllReviewsCont()
        
val rate: Int = ratingView.getRate()
        
ratingView.setOnRateChange(object : RatingView.OnRateChange {
    override fun onChange(rate: Float, totalReviewCount: Int, totalPoint: Int) {
                
    }
})
```
### Java
```java
// Review.STAR_5, Review.STAR_4, Review.STAR_3, Review.STAR_2 Review.STAR_1
ratingView.addReview(Review.STAR_1);

// Only Support of 1-5 number | فقط از اعداد 1 تا 5 پشتیبانی میکند
List<Integer> values = new ArrayList<>();
values.add(5);
values.add(3);
values.add(2);
ratingView.addReview(values);

int totalReviewCount = ratingView.getAllReviewsCont();

float rate = ratingView.getRate();

ratingView.setOnRateChange(new RatingView.OnRateChange() {
    @Override
    public void onChange(float rate, int totalReviewCount, int totalPoint) {

    }
});
```

## License

   Copyright (2020-2022) Farsroidx

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
