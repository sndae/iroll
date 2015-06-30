## (Website under construction) ##

This is the final project of the course CS4101(2009Fall): Introduction to Embedded Systems(嵌入式系統概論) in NTHU, Taiwan. What we've done actually covers the `[A.2]` option.


## iRoll Introduction ##
**iRoll** is an implementation of a gyroscope(陀螺儀) using only the normal vector(法向量) provided by the Openmoko sensor. We calculated the related physics data, then applied them to ball rolling simulation. Finally, we applied the ball rolling to a simple game similar to "Irritating Maze(電流急急棒，電流イライラ棒)".

We used NO Android 2D graphics, but very poor and primitive "ImageView + AbsoluteLayout" techniques by the way :p

The handling of image display and map information are totally independent. We must rely on the converted "map data" like [this](http://iroll.googlecode.com/svn/trunk/res/raw/map_r0_480x542.txt). (as well as the "ball data" for reachable area of an (x,y) point)


## System Requirements (Don't worry about this for standard Android phones) ##
  * Phone: **Openmoko Neo FreeRunner**, or Neo 1973 `[not tested]`.
  * Android image: The [Cupcake release](http://code.google.com/p/android-on-freerunner/) DOES NOT WORK with the sensor. A working one can be found in the [Downloads](http://code.google.com/p/iroll/downloads/list) page.
  * Android SDK: [Exactly 1.0](http://developer.android.com/intl/zh-TW/sdk/older_releases.html) for Linux, Windows, or Mac.
  * Programming IDE: Eclipse 3.5+ on Linux, Windows, or Mac.
  * API: 1.0, but I could not create Eclipse project until I installed the lowest available API 1.1. This eventually worked fine.
  * Burning: Linux, tested on Ubuntu 9.10 (64-bit). Follow section 4 of document 1 below, or follow [Jollen's blog](http://www.jollen.org/blog/2008/11/hello_moko_free_runner_android.html).

# iRoll is designed for ONLY one screen resolution 640 x 480 !! #
### This unfortunately, is NOT the same with any of the standard Android resolutions listed [here](http://developer.android.com/guide/practices/screens_support.html#testing). :(  But installing on standard Android phones should not be a problem. ###



---

## Introduction Video ##
Not available. :(

## A Winning Video (Time: 22.022 sec, 2010/01/22) ##
<a href='http://www.youtube.com/watch?feature=player_embedded&v=ie00JIIEkvY' target='_blank'><img src='http://img.youtube.com/vi/ie00JIIEkvY/0.jpg' width='640' height=480 /></a>

## My Best Record (Time: 16.820 sec, 2010/01/24) ##
<a href='http://www.youtube.com/watch?feature=player_embedded&v=lc3hLiMTZkM' target='_blank'><img src='http://img.youtube.com/vi/lc3hLiMTZkM/0.jpg' width='640' height=480 /></a>

## The Game Is Actually Quite Hard ##
<a href='http://www.youtube.com/watch?feature=player_embedded&v=5mv6mNWlVN8' target='_blank'><img src='http://img.youtube.com/vi/5mv6mNWlVN8/0.jpg' width='640' height=480 /></a>


## Sample Screenshots ##
1. Game start.

![http://iroll.googlecode.com/svn/trunk/pics/screenshot1.png](http://iroll.googlecode.com/svn/trunk/pics/screenshot1.png)

2. Get the first red ball (key 1).

![http://iroll.googlecode.com/svn/trunk/pics/screenshot2.png](http://iroll.googlecode.com/svn/trunk/pics/screenshot2.png)

3. Door 1 opened. Move back.

![http://iroll.googlecode.com/svn/trunk/pics/screenshot3.png](http://iroll.googlecode.com/svn/trunk/pics/screenshot3.png)

4. Before entering the Sharp Turn of Death.

![http://iroll.googlecode.com/svn/trunk/pics/screenshot4.png](http://iroll.googlecode.com/svn/trunk/pics/screenshot4.png)

5. Made it. Getting key 2.

![http://iroll.googlecode.com/svn/trunk/pics/screenshot5.png](http://iroll.googlecode.com/svn/trunk/pics/screenshot5.png)

6. Door 2 opened. Now the final Narrowing Alley.

![http://iroll.googlecode.com/svn/trunk/pics/screenshot6.png](http://iroll.googlecode.com/svn/trunk/pics/screenshot6.png)

7. Successfully reach the goal.

![http://iroll.googlecode.com/svn/trunk/pics/screenshot7.png](http://iroll.googlecode.com/svn/trunk/pics/screenshot7.png)


---

## The Source Code ##
  * The .apk binary file is available at the [Downloads](http://code.google.com/p/iroll/downloads/list) page. "adb install" should run fine on all host systems.
  * You can [browse](http://code.google.com/p/iroll/source/browse/) the source and see [changes](http://code.google.com/p/iroll/source/list) directly on this site.
  * To get the source and compile yourself, it is strongly recommended that you install the [Subclipse](http://subclipse.tigris.org/) plug-in (Subversion for Eclipse). After that, in Eclipse -> File -> Import -> SVN -> [iRoll SVN address](http://code.google.com/p/iroll/source/checkout) -> ...

## Documentations ##
  1. Here's a [tutorial](https://docs.google.com/Doc?docid=0AXoKc_12ppmlZGM1OTR6MjRfMTIzY2NxNG1uZHY) I wrote on setting up the environment to "get the sensor data" on FreeRunner phones.
  1. For math and ramp kinetics details, refer to document that might come up later.