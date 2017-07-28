@echo off
title    MTK Log �������� V1.0 
echo.
echo               *********************************************
echo               *                                           * 
echo               *          MTK Log �������� V1.0            *
echo               *                                           * 
echo               *      �˹�����Ҫ�û����а�װ ADB ����      * 
echo               *                                           * 
echo               *********************************************
echo.
:GotoCheckADB
call:CheckADBFun

:: do operate after adb  connect success 
echo.
echo               *********************************************
echo               *                                           * 
echo               *              ADB ���ӳɹ�                 *
echo               *                                           * 
echo               *********************************************
echo.              

::do choose operator
:GOTOLOOPCHOOSE
echo.
echo               * * * * * * * * * * * * * * * * * * * * * * *
echo               *                                           * 
echo               *  ��ѡ����Ҫ���еĲ���                     *
echo               *                                           * 
echo               *     1.���� MTK Log                        *
echo               *                                           * 
echo               *     2.�ر� MTK Log                        *
echo               *                                           * 
echo               *     3.���� MTK Log( �ֻ��洢)             *
echo               *                                           * 
echo               *     4.���� MTK Log( SD ��)                *
echo               *                                           * 
echo               *     5.���������� MTK Log(ץ����Log)       *
echo               *                                           * 
echo               *     6.ֹͣ���������� MTK Log              *
echo               *                                           * 
echo               *     7.�˳� ����                           *
echo               *                                           * 
echo               * * * * * * * * * * * * * * * * * * * * * * *
echo.

echo.
set/p  choose_operator=��������ѡ��Ĳ���(eg: 1-6):
echo ��ѡ��Ĳ����� :%choose_operator%
echo.
:: 1.���� MTK Log  
if %choose_operator% ==1 ( 
call:StartMTKLogFun
)

::2.�ر� MTK Log 
if %choose_operator% ==2 ( 
call:StopMTKLogFun
)

::3.���� MTK Log
if %choose_operator% ==3 (
call:ExportMTKLogFun
)

::4.����SD ���е�MTK Log 
if %choose_operator% ==4 (
call:ExportSDMTKLogFun
)

::5.���������� MTK Log(ץ����Log) 
if %choose_operator% ==5 (
call:AutoStartMTKLogFun
)

::6.ֹͣ���������� MTK Log 
if %choose_operator% ==6 (
call:StopAutoStartMTKLogFun
)

::7.�˳� ����
if %choose_operator% ==7 (
exit) else goto GOTOLOOPCHOOSE



:: 00.check ADB  status
:CheckADBFun
echo.
echo ��� ADB ���������......
echo.
adb devices | findstr "\<device\>"
echo.               
:: choose ADB status
IF ERRORLEVEL 1 goto NOADBCONNECTED 
GOTO:EOF


:: 00  ADB �޷����� 
:: do note user  on connect error 
:NOADBCONNECTED
echo.
echo               *********************************************
echo               *                                           * 
echo               *           ADB Connect Error               *
echo               *                                           * 
echo               *          ���� ADB �������              *
echo               *                                           * 
echo               *********************************************
echo.  
echo ��  �� : 
echo.
echo       1:ADB �Ƿ�װ�ڵ�����
echo           eg:
echo           DOS ϵͳ������ adb -version �鿴�Ƿ�������� ��
echo           ������������У������а�װ ADB
echo.
echo       2:��鿪����ѡ���� Debug �����Ƿ��
echo           eg:
echo           Settings -- Developer options -- USB debugging �Ƿ��
echo           ���û�п�����ѡ��뼤�� 
echo.
echo       3.�������ѡ��
echo           eg:
echo           ���� Settings -- About phone -- Build number
echo           ���������� "Build number" ���ɼ������ѡ��
echo.
echo       4.�����κ���������󣬿��Է����������ʼ�
echo           Email : jie.wangsprocomm.com   
echo.     
pause      
goto GotoCheckADB



:: 1.��ʼץȡLog
:StartMTKLogFun
call:CheckADBFun
echo               *********************************************
echo               *                                           * 
echo               *  ׼������MTK Log���Ե�Ƭ�̣�������......  *
echo               *                                           * 
echo               *********************************************
echo Starting ......
:: 1 2 4 16 �͵���23 �ֱ����MobileLog/ModemLog/NetworkLog/GPSLog  
:: external_sd  ����SD�� internal_sd ����SD�� 
::show_notification_1 show_notification_0 �򿪻�ر�״̬��
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
echo               *        MTK Log �����ɹ����븴������       *
echo               *   ������������ȹرգ�Ȼ�󵼳� MTK Log  * 
echo               *                                           *
echo               *    �˲���������Log����·�����ֻ��洢      *
echo               *   ����ѡ�� 3 ���������ֻ��洢�е� Log     *
echo               *                                           *
echo               *********************************************
echo.  
goto GOTOLOOPCHOOSE


::2.�ر�ץȡLog
:StopMTKLogFun
call:CheckADBFun
echo               *********************************************
echo               *                                           * 
echo               *  ׼��ֹͣ MTK Log���Ե�Ƭ�̣�ֹͣ��...... *
echo               *                                           * 
echo               *********************************************
echo Stoping ......
adb shell am broadcast -a com.mediatek.mtklogger.ADB_CMD -e cmd_name stop --ei cmd_target 23
echo.
echo               *********************************************
echo               *                                           * 
echo               *      �ɹ�ֹͣ MTK Log���뵼��Log          * 
echo               *                                           * 
echo               *********************************************
echo.  
goto GOTOLOOPCHOOSE

::3.���� Log
:ExportMTKLogFun
call:CheckADBFun
echo               *********************************************
echo               *                                           * 
echo               *  ׼������ MTK Log���Ե�Ƭ�̣�������...... *
echo               *                                           * 
echo               *********************************************

call:DeleteLogFileFun
adb pull /storage/emulated/0/mtklog  D:\MTKLogwj

echo.
echo               *********************************************
echo               *                                           * 
echo               *               Log �ɹ�����                * 
echo               *                                           * 
echo               *    Log ����·�� :   D:\MTKLogwj\mtklog    * 
echo               *                                           * 
echo               *********************************************
echo.  
goto GOTOLOOPCHOOSE

:: 4 ����SD��Log
:ExportSDMTKLogFun
call:CheckADBFun
echo               *********************************************
echo               *                                           * 
echo               *  ׼������ SD��Log���Ե�Ƭ�̣�������...... *
echo               *                                           * 
echo               *********************************************

call:DeleteLogFileFun
echo               *********************************************
echo               *                                           * 
echo               *    ���ڲ�ͬ����SD���������ֲ�һ��         * 
echo               *    ��Ҫ�ֶ�����MTK Loger �е�Log path     *
echo               *                                           * 
echo               *    ��ע���ֶ����� Log path ·��           * 
echo               *  (MTK Logger ��������������ʾLog path)    * 
echo               *                                           * 
echo               *********************************************
echo.
set /p  MTK_SD_PATH= ������MTK Log ����ʾ��Log path ·�� (eg: /storage/853E-1F02/mtklog/ ):
echo ���ѡ�� Log ·���� :%MTK_SD_PATH%
echo.
adb pull  %MTK_SD_PATH%  D:\MTKLogwj
if exist "D:\MTKLogwj\mtklog" ( 
echo. 
echo               *********************************************
echo               *              Log �ɹ�����                 * 
echo               *                                           * 
echo               *    Log ����·�� :   D:\MTKLogwj\mtklog    * 
echo               *                                           * 
echo               *********************************************
echo.   
) else ( 
echo.
echo               *********************************************
echo               *                                           * 
echo               *          Log ����ʧ�ܣ�������             * 
echo               *                                           * 
echo               *    1.����Log path �Ƿ�������ȷ          * 
echo               *        eg ��MTK Logger ���������Log path *
echo               *             ��ͬSD·����һ�������ֶ�����  *
echo               *        eg ��                              * 
echo               *       Log path��/storage/853E-1F02/mtklog/* 
echo               *                                           * 
echo               *    2.���� SD�� ״̬                     *
echo               *        eg: SD���Ƿ�����ҿ�������ʹ��     * 
echo               *                                           *  
echo               *    3.���� SD�� ���Ƿ���mtklog�ļ���     *
echo               *        eg: �ļ������� -- �鿴SD������     * 
echo               *                                           * 
echo               *    Log ����·�� :   D:\MTKLogwj\mtklog    *
echo               *                                           *  
echo               *********************************************
echo.  
  )
goto GOTOLOOPCHOOSE



::����������
:AutoStartMTKLogFun
call:CheckADBFun
echo               *********************************************
echo               *                                           * 
echo               *  ����������ģʽ(ץ����Log)��������......  *
echo               *                                           * 
echo               *********************************************
::set_auto_start_1��ʾ����������������set_auto_start_0��ʾ�رտ���������
adb shell am broadcast -a com.mediatek.mtklogger.ADB_CMD -e cmd_name set_auto_start_0 --ei cmd_target 23
adb shell am broadcast -a com.mediatek.mtklogger.ADB_CMD -e cmd_name set_auto_start_1 --ei cmd_target 23
echo.
echo               *********************************************
echo               *                                           * 
echo               *           �ɹ����ÿ���������ģʽ          *
echo               *                                           * 
echo               *   ����������ģʽ���ֻ��´�����ʱ�Զ�����  *
echo               *                                           *
echo               *              ��ע�������ֻ�               *
echo               *                                           * 
echo               *********************************************
echo.

echo.
:: �ж��Ƿ�Ҫ���������ֻ�
echo.
set /p  isRebootPhone= ȷ��Ҫ�����ֻ�ô (eg: yes / no ):
echo ���ѡ���� :%isRebootPhone%
echo.
if %isRebootPhone%== yes (
call:CheckADBFun 
adb reboot
)else goto GOTOLOOPCHOOSE

::�رտ���������
:StopAutoStartMTKLogFun
call:CheckADBFun
echo               *********************************************
echo               *                                           * 
echo               *       ֹͣ����������ģʽ,ֹͣ��......     *
echo               *                                           * 
echo               *********************************************
adb shell am broadcast -a com.mediatek.mtklogger.ADB_CMD -e cmd_name set_auto_start_0 --ei cmd_target 23
echo.
echo               *********************************************
echo               *                                           * 
echo               *        �ɹ�ֹͣ����������ģʽ             *
echo               *                                           * 
echo               *********************************************
echo.  

:: ��D�̴��� Log ����Ŀ¼�������Ŀ¼���ڣ���ɾ����Ȼ�󴴽�Ŀ¼
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

