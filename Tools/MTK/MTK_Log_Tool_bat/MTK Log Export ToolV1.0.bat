@echo off
title    MTK Log 导出工具 V1.0 
echo.
echo               *********************************************
echo               *                                           * 
echo               *          MTK Log 导出工具 V1.0            *
echo               *                                           * 
echo               *      此工具需要用户自行安装 ADB 环境      * 
echo               *                                           * 
echo               *********************************************
echo.
:GotoCheckADB
call:CheckADBFun

:: do operate after adb  connect success 
echo.
echo               *********************************************
echo               *                                           * 
echo               *              ADB 连接成功                 *
echo               *                                           * 
echo               *********************************************
echo.              

::do choose operator
:GOTOLOOPCHOOSE
echo.
echo               * * * * * * * * * * * * * * * * * * * * * * *
echo               *                                           * 
echo               *  请选择你要进行的操作                     *
echo               *                                           * 
echo               *     1.开启 MTK Log                        *
echo               *                                           * 
echo               *     2.关闭 MTK Log                        *
echo               *                                           * 
echo               *     3.导出 MTK Log( 手机存储)             *
echo               *                                           * 
echo               *     4.导出 MTK Log( SD 卡)                *
echo               *                                           * 
echo               *     5.开机自启动 MTK Log(抓开机Log)       *
echo               *                                           * 
echo               *     6.停止开机自启动 MTK Log              *
echo               *                                           * 
echo               *     7.退出 工具                           *
echo               *                                           * 
echo               * * * * * * * * * * * * * * * * * * * * * * *
echo.

echo.
set/p  choose_operator=请输入你选择的操作(eg: 1-6):
echo 你选择的操作是 :%choose_operator%
echo.
:: 1.开启 MTK Log  
if %choose_operator% ==1 ( 
call:StartMTKLogFun
)

::2.关闭 MTK Log 
if %choose_operator% ==2 ( 
call:StopMTKLogFun
)

::3.导出 MTK Log
if %choose_operator% ==3 (
call:ExportMTKLogFun
)

::4.导出SD 卡中的MTK Log 
if %choose_operator% ==4 (
call:ExportSDMTKLogFun
)

::5.开机自启动 MTK Log(抓开机Log) 
if %choose_operator% ==5 (
call:AutoStartMTKLogFun
)

::6.停止开机自启动 MTK Log 
if %choose_operator% ==6 (
call:StopAutoStartMTKLogFun
)

::7.退出 工具
if %choose_operator% ==7 (
exit) else goto GOTOLOOPCHOOSE



:: 00.check ADB  status
:CheckADBFun
echo.
echo 检查 ADB 连接情况中......
echo.
adb devices | findstr "\<device\>"
echo.               
:: choose ADB status
IF ERRORLEVEL 1 goto NOADBCONNECTED 
GOTO:EOF


:: 00  ADB 无法连接 
:: do note user  on connect error 
:NOADBCONNECTED
echo.
echo               *********************************************
echo               *                                           * 
echo               *           ADB Connect Error               *
echo               *                                           * 
echo               *          请检查 ADB 连接情况              *
echo               *                                           * 
echo               *********************************************
echo.  
echo 例  如 : 
echo.
echo       1:ADB 是否安装在电脑上
echo           eg:
echo           DOS 系统下输入 adb -version 查看是否可以运行 
echo           如果不可以运行，请自行安装 ADB
echo.
echo       2:检查开发者选项中 Debug 开关是否打开
echo           eg:
echo           Settings -- Developer options -- USB debugging 是否打开
echo           如果没有开发者选项，请激活 
echo.
echo       3.激活开发者选项
echo           eg:
echo           进入 Settings -- About phone -- Build number
echo           多次连续点击 "Build number" 即可激活开发者选项
echo.
echo       4.如有任何问题或需求，可以发送至以下邮件
echo           Email : jie.wangsprocomm.com   
echo.     
pause      
goto GotoCheckADB



:: 1.开始抓取Log
:StartMTKLogFun
call:CheckADBFun
echo               *********************************************
echo               *                                           * 
echo               *  准备开启MTK Log，稍等片刻，开启中......  *
echo               *                                           * 
echo               *********************************************
echo Starting ......
:: 1 2 4 16 和等于23 分别代表MobileLog/ModemLog/NetworkLog/GPSLog  
:: external_sd  外置SD卡 internal_sd 内置SD卡 
::show_notification_1 show_notification_0 打开或关闭状态栏
adb shell am broadcast -a com.mediatek.mtklogger.ADB_CMD -e cmd_name stop --ei cmd_target 23
adb shell am broadcast -a com.mediatek.mtklogger.ADB_CMD -e cmd_name set_auto_start_0 --ei cmd_target 23
adb shell am broadcast -a com.mediatek.mtklogger.ADB_CMD -e cmd_name clear_all_logs
adb shell am broadcast -a com.mediatek.mtklogger.ADB_CMD -e cmd_name switch_logpath -e cmd_target internal_sd
adb shell am broadcast -a com.mediatek.mtklogger.ADB_CMD -e cmd_name start --ei cmd_target 23
adb shell am start -n com.mediatek.mtklogger/com.mediatek.mtklogger.MainActivity
adb shell am broadcast -a com.mediatek.mtklogger.ADB_CMD -e cmd_name show_notification_1 --ei cmd_target -1
echo.
echo               *********************************************
echo               *                                           * 
echo               *        MTK Log 开启成功，请复现问题       *
echo               *   复现问题后，请先关闭，然后导出 MTK Log  * 
echo               *                                           *
echo               *    此操作开启的Log保存路径：手机存储      *
echo               *   请用选择 3 操作导出手机存储中的 Log     *
echo               *                                           *
echo               *********************************************
echo.  
goto GOTOLOOPCHOOSE


::2.关闭抓取Log
:StopMTKLogFun
call:CheckADBFun
echo               *********************************************
echo               *                                           * 
echo               *  准备停止 MTK Log，稍等片刻，停止中...... *
echo               *                                           * 
echo               *********************************************
echo Stoping ......
adb shell am broadcast -a com.mediatek.mtklogger.ADB_CMD -e cmd_name stop --ei cmd_target 23
echo.
echo               *********************************************
echo               *                                           * 
echo               *      成功停止 MTK Log，请导出Log          * 
echo               *                                           * 
echo               *********************************************
echo.  
goto GOTOLOOPCHOOSE

::3.导出 Log
:ExportMTKLogFun
call:CheckADBFun
echo               *********************************************
echo               *                                           * 
echo               *  准备导出 MTK Log，稍等片刻，导出中...... *
echo               *                                           * 
echo               *********************************************

call:DeleteLogFileFun
adb pull /storage/emulated/0/mtklog  D:\MTKLogwj

echo.
echo               *********************************************
echo               *                                           * 
echo               *               Log 成功导出                * 
echo               *                                           * 
echo               *    Log 保存路径 :   D:\MTKLogwj\mtklog    * 
echo               *                                           * 
echo               *********************************************
echo.  
goto GOTOLOOPCHOOSE

:: 4 导出SD卡Log
:ExportSDMTKLogFun
call:CheckADBFun
echo               *********************************************
echo               *                                           * 
echo               *  准备导出 SD卡Log，稍等片刻，导出中...... *
echo               *                                           * 
echo               *********************************************

call:DeleteLogFileFun
echo               *********************************************
echo               *                                           * 
echo               *    由于不同厂商SD卡挂载名字不一样         * 
echo               *    需要手动输入MTK Loger 中的Log path     *
echo               *                                           * 
echo               *    请注意手动输入 Log path 路径           * 
echo               *  (MTK Logger 主界面下面有提示Log path)    * 
echo               *                                           * 
echo               *********************************************
echo.
set /p  MTK_SD_PATH= 请输入MTK Log 中显示的Log path 路径 (eg: /storage/853E-1F02/mtklog/ ):
echo 你的选择 Log 路径是 :%MTK_SD_PATH%
echo.
adb pull  %MTK_SD_PATH%  D:\MTKLogwj
if exist "D:\MTKLogwj\mtklog" ( 
echo. 
echo               *********************************************
echo               *              Log 成功导出                 * 
echo               *                                           * 
echo               *    Log 保存路径 :   D:\MTKLogwj\mtklog    * 
echo               *                                           * 
echo               *********************************************
echo.   
) else ( 
echo.
echo               *********************************************
echo               *                                           * 
echo               *          Log 导出失败，请重试             * 
echo               *                                           * 
echo               *    1.请检查Log path 是否输入正确          * 
echo               *        eg ：MTK Logger 最下面会有Log path *
echo               *             不同SD路径不一样，需手动输入  *
echo               *        eg ：                              * 
echo               *       Log path：/storage/853E-1F02/mtklog/* 
echo               *                                           * 
echo               *    2.请检查 SD卡 状态                     *
echo               *        eg: SD卡是否插入且可以正常使用     * 
echo               *                                           *  
echo               *    3.请检查 SD卡 中是否有mtklog文件夹     *
echo               *        eg: 文件管理器 -- 查看SD卡内容     * 
echo               *                                           * 
echo               *    Log 保存路径 :   D:\MTKLogwj\mtklog    *
echo               *                                           *  
echo               *********************************************
echo.  
  )
goto GOTOLOOPCHOOSE



::开机自启动
:AutoStartMTKLogFun
call:CheckADBFun
echo               *********************************************
echo               *                                           * 
echo               *  开机自启动模式(抓开机Log)，启动中......  *
echo               *                                           * 
echo               *********************************************
::set_auto_start_1表示开启开机自启动，set_auto_start_0表示关闭开机自启动
adb shell am broadcast -a com.mediatek.mtklogger.ADB_CMD -e cmd_name set_auto_start_0 --ei cmd_target 23
adb shell am broadcast -a com.mediatek.mtklogger.ADB_CMD -e cmd_name set_auto_start_1 --ei cmd_target 23
echo.
echo               *********************************************
echo               *                                           * 
echo               *           成功设置开机自启动模式          *
echo               *                                           * 
echo               *   开机自启动模式在手机下次重启时自动开启  *
echo               *                                           *
echo               *              请注意重启手机               *
echo               *                                           * 
echo               *********************************************
echo.

echo.
:: 判断是否要重新启动手机
echo.
set /p  isRebootPhone= 确认要重启手机么 (eg: yes / no ):
echo 你的选择是 :%isRebootPhone%
echo.
if %isRebootPhone%== yes (
call:CheckADBFun 
adb reboot
)else goto GOTOLOOPCHOOSE

::关闭开机自启动
:StopAutoStartMTKLogFun
call:CheckADBFun
echo               *********************************************
echo               *                                           * 
echo               *       停止开机自启动模式,停止中......     *
echo               *                                           * 
echo               *********************************************
adb shell am broadcast -a com.mediatek.mtklogger.ADB_CMD -e cmd_name set_auto_start_0 --ei cmd_target 23
echo.
echo               *********************************************
echo               *                                           * 
echo               *        成功停止开机自启动模式             *
echo               *                                           * 
echo               *********************************************
echo.  

:: 在D盘创建 Log 导出目录，如果此目录存在，先删除，然后创建目录
:DeleteLogFileFun
if exist  "D:\MTKLogwj" ( del /f /q /a  D:\MTKLogwj\*.*
rd /q /s  D:\MTKLogwj)
md  D:\MTKLogwj
echo Exporting ......
adb pull data/anr/traces.txt  D:\MTKLogwj
adb pull /system/build.prop  D:\MTKLogwj
adb shell df  >  D:\MTKLogwj\df.txt
adb shell cat  /proc/meminfo  > D:\MTKLogwj\meminfo.txt
adb shell getprop > D:\MTKLogwj\SystemProp.txt
adb shell pm list packages > D:\MTKLogwj\PackageInstall.txt

