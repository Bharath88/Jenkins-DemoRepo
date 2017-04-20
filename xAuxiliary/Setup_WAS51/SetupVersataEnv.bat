@echo on
REM  ************************************************************************
REM  * SetupVersataEnv.bat - Modification log
REM  * ======================================================================
REM  * 01/04/02   Sid   Version 1.0 created
REM  * 01/25/02   Sid   Copy Client\lib components
REM  * 01/28/02   Sid   Copy utils for registering objects in Versata Studio
REM  * 02/14/02   Sid   Copy temporary Versata Cache sources & their Classes
REM  * 08/22/02 Surabhi Modified for Versata 5.5
REM  * 03/11/03   IG    Added file copy for setting Versata Studio Classpaths
REM  ************************************************************************

@echo About to update your WebSphere installation dir %WEBSPHERE_HOME%,
@echo your Versata installation dir %VERSATA_HOME%
@echo your ADVANTAGE development dir %AMSADV3DEV_HOME%
@echo and your ADVANTAGE library dir %AMSADV3LIB_HOME%.
@echo Must for ADVANTAGE Application to compile and run.

:START
@echo [1]Continue?
@echo [2]More Info?
@echo [3]Do you want to skip this process?

Choice /c:123
IF ERRORLEVEL 1 GOTO DOSTUFF
IF ERRORLEVEL 2 GOTO HELP
IF ERRORLEVEL 3 GOTO SKIP

:HELP
@echo More Information - Following files are updated in your WebSphere/Versata/Advantage Installation directories:
@echo 1. Copy/Update various Template files
@echo 2. Copy/Update various Initialization files
@echo 3. Copy/Update various WebSphere/Versata files
@echo 4. Create Runtime directories

GOTO START

:DOSTUFF

REM  **********************************************************
REM  * Copy Versata master.vdb
REM  **********************************************************
@echo Copying Versata master.vdb...
COPY %AMSADV3APP_HOME%\Versata55_50\master.vdb %VERSATA_HOME%\master.vdb

REM  **********************************************************
REM  * Copy Versata Templates
REM  **********************************************************
@echo Copying Versata Template files...
COPY %AMSADV3APP_HOME%\Versata55_50\RuleArchetypes\Java\Corba\JavaComponent.tpl %VERSATA_HOME%\RuleArchetypes\Java\Corba\JavaComponent.tpl
COPY %AMSADV3APP_HOME%\Versata55_50\RuntimeJava\Archetypes\*.tpl %VERSATA_HOME%\RuntimeJava\Archetypes
COPY %AMSADV3APP_HOME%\Versata55_50\RuntimeHtml\Archetypes\*.tpl %VERSATA_HOME%\RuntimeHtml\Archetypes

REM  **********************************************************
REM  * Copy Versata libraries
REM  **********************************************************
@echo Copying Versata files...
XCOPY %AMSADV3APP_HOME%\Versata55_50\VLS\lib\*.* %VERSATA_HOME%\VLS\lib\*.* /s /q
XCOPY %AMSADV3APP_HOME%\Versata55_50\VLS\lib\*.* %VERSATA_HOME%\Config\mydefault.ear\*.* /s /q

@echo Copying Versata default EAR files...
XCOPY %AMSADV3APP_HOME%\Versata55_50\Config\mydefault.ear\*.* %VERSATA_HOME%\Config\mydefault.ear\*.* /s /q
XCOPY %AMSADV3APP_HOME%\Versata55_50\Config\mydefault.ear\META-INF\*.* %VERSATA_HOME%\Config\mydefault.ear\META-INF\*.* /s /q

@echo Copying Versata VFC files...
XCOPY %AMSADV3APP_HOME%\Versata55_50\Client\lib\*.* %VERSATA_HOME%\Client\lib\*.* /s /q
XCOPY %AMSADV3APP_HOME%\Versata55_50\Client\lib\*.jar %VERSATA_HOME%\Config\mydefault.ear\*.jar /s /q

REM  **********************************************************
REM  * Copy VLSConsoleEJB.bat
REM  **********************************************************
@echo Copying Deploy files...
COPY %AMSADV3APP_HOME%\Versata55_50\VLS\bin\default-ver-vlsconsoleejb_bat\VLSConsoleEJB.bat %VERSATA_HOME%\VLS\bin

REM  **********************************************************
REM  * Copy Performance Trace Config file
REM  **********************************************************
@echo Copying Perf Trace Config file
COPY %AMSADV3APP_HOME%\Versata55_50\VLS\bin\AMSTraceConfig.properties %VERSATA_HOME%\VLS\bin

REM  **********************************************************
REM  * Copy ADVANTAGE 3.3 ini files
REM  **********************************************************
@echo Copying ADVANTAGE 3.3 ini files...
COPY %AMSADV3APP_HOME%\Versata55_50\vls\bin\default-ver-ADV30Params\ADV*.ini %VERSATA_HOME%\Vls\bin

REM  **********************************************************
REM  * Copy ADVANTAGE 3.3 properties files
REM  **********************************************************
@echo Copying ADVANTAGE 3.3 properties files...
COPY %AMSADV3LIB_HOME%\CSF.properties %VERSATA_HOME%\Vls\bin

REM  **********************************************************
REM  * Copy AppConfig properties & EJBAppServerConfig.txt files
REM  **********************************************************
@echo Copying Config files...
COPY %AMSADV3APP_HOME%\Versata55_50\VlsComponents\default-ver-EJBAppServerConfig\AppConfig.properties %VERSATA_HOME%\VlsComponents
COPY %AMSADV3APP_HOME%\Versata55_50\VlsComponents\default-ver-EJBAppServerConfig\EJBAppServerConfig.txt %VERSATA_HOME%\VlsComponents

REM  **********************************************************
REM  * Copy Security libraries and configuration
REM  **********************************************************
@echo Copying Security files...
XCOPY %AMSADV3APP_HOME%\WebSphere50\AppServer\java\jre\lib\ext\*.* %WEBSPHERE_HOME%\java\jre\lib\ext\*.* /s /q
COPY %AMSADV3APP_HOME%\WebSphere50\AppServer\java\jre\lib\security\java.security %WEBSPHERE_HOME%\java\jre\lib\security\

REM  **********************************************************
REM  * Create Runtime directories
REM  **********************************************************
@echo Create Runtime directories...
MD %VERSATA_HOME%\VlsComponents\Classes
XCOPY %AMSADV3APP_HOME%\Versata55_50\VLSComponents\Classes\*.* %VERSATA_HOME%\VlsComponents\Classes /s /q

CD %AMSADV3DEV_HOME%\
CD ..
MD RTFiles
CD RTFiles
MD VLS1
CD VLS1
MD DocArchives
MD Logs
MD ExportImport
rem MD Export
rem MD Import
MD ReportLayout
MD ReportOutput
CD ReportOutput
MD Reports
rem CD ..\Import
rem MD Error
CD ..\ExportImport
MD ImportError
CD \

REM  **********************************************************
REM  * Copy utils for registering objects in Versata Studio
REM  **********************************************************
REM @echo Copying utils for registering objects in Versata Studio...
COPY %AMSADV3APP_HOME%\Versata55_50\ClassInfo\AMSRegisterObjects.bat %VERSATA_HOME%\ClassInfo

REM  **********************************************************
REM  * Copy VFCResource.properties to Versata directory
REM  * ******* Uncomment if required
REM  **********************************************************
@echo Copying VFCResource.properties to Versata...
COPY %AMSADV3APP_HOME%\Versata55_50\Client\lib\VFCResource.properties %VERSATA_HOME%\Client\lib

REM  **********************************************************
REM  * Copy setup files for Versata Studio classpath to Dev
REM  **********************************************************
@echo Copying files for setting Versata Studio classpath...
COPY %AMSADV3AUX_HOME%\Setup\Versata_Studio_Classpaths.reg %AMSADV3DEV_HOME%
COPY %AMSADV3AUX_HOME%\Setup\Versata_Studio_Classpaths.txt %AMSADV3DEV_HOME%

@echo **********************************
@echo * WebSphere/Versata/ADVANTAGE 3.3 istallation has been updated
@echo **********************************

GOTO END

:SKIP
@echo WebSphere/Versata/ADVANTAGE 3.3 installation has not been updated

:END
