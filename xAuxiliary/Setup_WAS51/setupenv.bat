@echo off
REM  ************************************************************************
REM  * SetupEnv.bat - Modification log
REM  * ======================================================================
REM  * 01/04/02	Sid	Version 1.0 created
REM  ************************************************************************

REM ***********************************************************
REM ** Modify as per installation requirements
REM ***********************************************************
SET AMSADV3APP_HOME=C:\AMSADV3\AMSAPPS
SET AMSADV3DEV_HOME=C:\AMSADV3\dev
SET AMSADV3LIB_HOME=C:\AMSADV3\lib
SET AMSADV3AUX_HOME=C:\AMSADV3\xAuxilary
SET VERSATA_HOME=C:\AMSAPPS\Versata56_51
SET WEBSPHERE_HOME=C:\AMSAPPS\WebSphere51\AppServer
REM *********************************************************************

@echo *******************************************************************
@echo The ADVANTAGE development directory is set to %AMSADV3DEV_HOME%, 
@echo The ADVANTAGE library directory is set to %AMSADV3LIB_HOME%, 
@echo the Versata Install directory is set to %VERSATA_HOME% 
@echo and the WebSphere Install directory is set to %WEBSPHERE_HOME%.
@echo Modify settings if these settings do not match your installation.
@echo *******************************************************************

REM *********************************************************************
REM * Update Versata environment
REM *********************************************************************
@echo Updating Versata Environment ...
call SetupVersataEnv.bat

REM *********************************************************************
REM * Merging the Server And Client Inf
REM *********************************************************************
REM @echo Merging Server and Client Inf...

REM call merge.bat %SERVER_INF_HOME% %CLIENT_INF_HOME% %AMSADV3DEV_HOME%
REM *********************************************************************
REM * Call the XML Merge utility
REM *********************************************************************
REM @echo Starting XML merge ...
REM call MergeJadeRep.bat

REM *********************************************************************
REM * Msg to the user
REM *********************************************************************
@echo Following steps must be completed to run the ADVANTAGE Application. 
@echo 
@echo 1. Update the ADV30Params.ini file in %VERSATA_HOME%\Vls\bin directory
@echo 2. Update Security information in Versata EJB Console.

SET AMSADV3DEV_HOME=
SET AMSADV3LIB_HOME=
SET VERSATA_HOME=
SET WEBSPHERE_HOME=

pause
