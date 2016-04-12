/**
 *  Amcrest Camera
 *
 *  Copyright � 2016 David Guindon, (Belgarion on SmartThings)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 *  You are free to use this code, provided the following conditions are met:
 *   - This software is free for Private Use.  You may use and modify the software without distributing it.
 *   - This software and any derivatives of it may not be used for commercial purposes.
 *   - The images and/or files that originate from "http://smartthings.belgarion.s3.amazonaws.com/" are for use within
 *     THIS code only and any reference to these resources beyond this use is expressly prohibited.
 *
 *  Citations:
 *   - Patrick Stuart: Based on all of the hard work he put into his Generic Camera device.  Very educational!
 *   - tgauchat and RBoy: For the "convertHostnameToIPAddress" macro.
 *   - slagle, eparkerjr and scottinpollock: For their efforts on the great Foscam & D-Link device types.
 *   - RBoy: Thank you for the code for making the JPEG image available to other apps.
 *
 *  Release History:
 *    2016-04-10: v1.0.0 = Initial release
 *
 **/

metadata {
    definition (name: "Amcrest Camera", namespace: "belgarion", author: "Belgarion") {
        capability "Image Capture"
        capability "Motion Sensor"
        capability "Polling"
        capability "Refresh"
        capability "Sensor"
        capability "Switch"
        capability "Switch Level"

        attribute "hubactionMode", "string"
        attribute "imageDataJpeg", "string"

        command "changeNvLED"
        command "changeRecord"
        command "changeRotation"
        command "lightOff"
        command "lightOn"
        command "moveDown"
        command "moveLeft"
        command "moveRight"
        command "moveUp"
        command "presetCmd1"
        command "presetCmd2"
        command "presetCmd3"
        command "presetCmd4"
        command "presetCmd5"
        command "presetCmd6"
        command "rebootNow"
        command "setLevelSpeed"
        command "setLevelSensitivity"
        command "toggleFlip"
        command "toggleMirror"
        command "toggleMotion"
        command "zoomIn"
        command "zoomOut"
    }

    preferences {
        input("camIP", "string", title:"Hostname or IP Address", description: "Enter the Hostname or IP Address of the camera", required: true, displayDuringSetup: true)
        input("camPort", "string", title:"Port", description: "Enter the Port Number assigned to the camera", defaultValue: 80 , required: true, displayDuringSetup: true)
        input("camUser", "string", title:"Account Username", description: "Enter the Account Username to use", required: true, displayDuringSetup: true)
        input("camPassword", "password", title:"Account Password", description: "Enter the Password for this Account Username", required: true, displayDuringSetup: true)
        input("camChannel", "range: 0..9", title:"Video Channel", description: "Specify the image channel to use (typically 0)", defaultValue: 0, required: true, displayDuringSetup: true)
        input("camDebug", "bool", title:"Camera Debug Mode", description: "Enable to display debugging information in the 'Live Logging' view", defaultValue: false, required: false, displayDuringSetup: true)
    }


    simulator {
        //
    }


    tiles(scale: 2) {
        standardTile("take", "device.image", width: 2, height: 2, canChangeIcon: false, canChangeBackground: false, decoration: "flat") {
            state "take", label: "Take", action: "Image Capture.take", icon: "http://smartthings.belgarion.s3.amazonaws.com/images/IPM-721S.png", backgroundColor: "#FFFFFF", nextState:"taking"
            state "taking", label:"Taking", action: "", icon: "st.camera.take-photo", backgroundColor: "#53a7c0"
            state "image", label: "Take", action: "Image Capture.take", icon: "st.camera.camera", backgroundColor: "#FFFFFF", nextState:"taking"
        }
        standardTile("ledState", "device.ledStatus", width: 2, height: 2, canChangeIcon: false, canChangeBackground: false, decoration: "flat") {
            state "off", label: "", action: "changeNvLED", icon: "http://smartthings.belgarion.s3.amazonaws.com/images/IR-LED-Off.png", backgroundColor: "#FFFFFF", nextState: "..."
            state "on", label: "", action: "changeNvLED", icon: "http://smartthings.belgarion.s3.amazonaws.com/images/IR-LED-On.png", backgroundColor: "#FFFFFF", nextState: "..."
            state "auto", label: "", action: "changeNvLED", icon: "http://smartthings.belgarion.s3.amazonaws.com/images/IR-LED-Auto.png", backgroundColor: "#FFFFFF", nextState: "..."
            state "...", label: "...", action: "", nextState: "..."
        }
        standardTile("motionStatus", "device.motionStatus", width: 2, height: 2, canChangeIcon: false, decoration: "flat") {
            state "off", label: "", action: "toggleMotion", icon: "http://smartthings.belgarion.s3.amazonaws.com/images/Motion-Off.png", backgroundColor: "#FFFFFF", nextState: "..."
            state "on", label: "", action: "toggleMotion", icon: "http://smartthings.belgarion.s3.amazonaws.com/images/Motion-On.png", backgroundColor: "#FFFFFF", nextState: "..."
            state "...", label: "...", action: "", nextState: "..."
        }
        carouselTile("camDetails", "device.image", width: 6, height: 4) {
        }
        standardTile("preset1", "device.presetStatus", width: 1, height: 1, canChangeIcon: false, canChangeBackground: false, decoration: "flat") {
            state "preset1", label: "", action: "presetCmd1", icon: "http://smartthings.belgarion.s3.amazonaws.com/images/Preset1-ON.png", backgroundColor: "#FFFFFF", nextState: "preset1On"
            state "preset1On", label: "", action: "", icon: "http://smartthings.belgarion.s3.amazonaws.com/images/Preset1-OFF.png", backgroundColor: "#FFFFFF", nextState: "preset1"
        }
        standardTile("preset2", "device.presetStatus", width: 1, height: 1, canChangeIcon: false, canChangeBackground: false, decoration: "flat") {
            state "preset2", label: "", action: "presetCmd2", icon: "http://smartthings.belgarion.s3.amazonaws.com/images/Preset2-ON.png", backgroundColor: "#FFFFFF", nextState: "preset2On"
            state "preset2On", label: "", action: "", icon: "http://smartthings.belgarion.s3.amazonaws.com/images/Preset2-OFF.png", backgroundColor: "#FFFFFF", nextState: "preset2"
        }
        standardTile("preset3", "device.presetStatus", width: 1, height: 1, canChangeIcon: false, canChangeBackground: false, decoration: "flat") {
            state "preset3", label: "", action: "presetCmd3", icon: "http://smartthings.belgarion.s3.amazonaws.com/images/Preset3-ON.png", backgroundColor: "#FFFFFF", nextState: "preset3On"
            state "preset3On", label: "", action: "", icon: "http://smartthings.belgarion.s3.amazonaws.com/images/Preset3-OFF.png", backgroundColor: "#FFFFFF", nextState: "preset3"
        }
        standardTile("preset4", "device.presetStatus", width: 1, height: 1, canChangeIcon: false, canChangeBackground: false, decoration: "flat") {
            state "preset4", label: "", action: "presetCmd4", icon: "http://smartthings.belgarion.s3.amazonaws.com/images/Preset4-ON.png", backgroundColor: "#FFFFFF", nextState: "preset4On"
            state "preset4On", label: "", action: "", icon: "http://smartthings.belgarion.s3.amazonaws.com/images/Preset4-OFF.png", backgroundColor: "#FFFFFF", nextState: "preset4"
        }
        standardTile("preset5", "device.presetStatus", width: 1, height: 1, canChangeIcon: false, canChangeBackground: false, decoration: "flat") {
            state "preset5", label: "", action: "presetCmd5", icon: "http://smartthings.belgarion.s3.amazonaws.com/images/Preset5-ON.png", backgroundColor: "#FFFFFF", nextState: "preset5On"
            state "preset5On", label: "", action: "", icon: "http://smartthings.belgarion.s3.amazonaws.com/images/Preset5-OFF.png", backgroundColor: "#FFFFFF", nextState: "preset5"
        }
        standardTile("preset6", "device.presetStatus", width: 1, height: 1, canChangeIcon: false, canChangeBackground: false, decoration: "flat") {
            state "preset6", label: "", action: "presetCmd6", icon: "http://smartthings.belgarion.s3.amazonaws.com/images/Preset6-ON.png", backgroundColor: "#FFFFFF", nextState: "preset6On"
            state "preset6On", label: "", action: "", icon: "http://smartthings.belgarion.s3.amazonaws.com/images/Preset6-OFF.png", backgroundColor: "#FFFFFF", nextState: "preset6"
        }
        standardTile("zoomOut", "device.zoomOut", width: 2, height: 2, canChangeIcon: false, canChangeBackground: false) {
            state "zoomOut", label: "", action: "zoomOut", icon: "st.secondary.less", backgroundColor: "#FFFFFF", nextState: "zoomingOut"
            state "zoomingOut", label: "", action: "", icon: "st.secondary.less", backgroundColor: "#00FF00", nextState: "zoomOut"
        }
        standardTile("up", "device.up", width: 2, height: 2, canChangeIcon: false, canChangeBackground: false, decoration: "flat") {
            state "up", label: "Up", action: "moveUp", icon: "st.thermostat.thermostat-up", backgroundColor: "#FFFFFF", nextState: "upish"
            state "upish", label: "Up", action: "", icon: "st.thermostat.thermostat-up", backgroundColor: "#00FF00", nextState: "up"
        }
        standardTile("zoomIn", "device.zoomIn", width: 2, height: 2, canChangeIcon: false, canChangeBackground: false) {
            state "zoomIn", label: "", action: "zoomIn", icon: "st.secondary.more", backgroundColor: "#FFFFFF", nextState: "zoomingIn"
            state "zoomingIn", label: "", action: "", icon: "st.secondary.more", backgroundColor: "#00FF00", nextState: "zoomIn"
        }
        standardTile("left", "device.left", width: 2, height: 2, canChangeIcon: false, canChangeBackground: false, decoration: "flat") {
            state "left", label: "Left", action: "moveLeft", icon: "st.thermostat.thermostat-left", backgroundColor: "#FFFFFF", nextState: "leftish"
            state "leftish", label: "Left", action: "", icon: "st.thermostat.thermostat-left", backgroundColor: "#00FF00", nextState: "left"
        }
        standardTile("refresh", "device.switch", width: 2, height: 2, canChangeIcon: false, canChangeBackground: false) {
            state "refresh", label: "Refresh", action: "refresh.refresh", icon: "st.secondary.refresh", backgroundColor: "#FFFFFF"
        }
        standardTile("right", "device.right", width: 2, height: 2, canChangeIcon: false, canChangeBackground: false, decoration: "flat") {
            state "right", label: "Right", action: "moveRight", icon: "st.thermostat.thermostat-right", backgroundColor: "#FFFFFF", nextState: "rightish"
            state "rightish", label: "Right", action: "", icon: "st.thermostat.thermostat-right", backgroundColor: "#00FF00", nextState: "right"
        }
        standardTile("recState", "device.recStatus", width: 2, height: 2, canChangeIcon: false, canChangeBackground: false, decoration: "flat") {
            state "off", label: "Record Off", action: "changeRecord", icon: "st.Electronics.electronics7", backgroundColor: "#FFFFFF", nextState: "..."
            state "on", label: "Record On", action: "changeRecord", icon: "st.Electronics.electronics7", backgroundColor: "#FFFFFF", nextState: "..."
            state "auto", label: "Record Auto", action: "changeRecord", icon: "st.Electronics.electronics7", backgroundColor: "#FFFFFF", nextState: "..."
            state "...", label: "...", action: "", nextState: "..."
        }
        standardTile("down", "device.down", width: 2, height: 2, canChangeIcon: false, canChangeBackground: false, decoration: "flat") {
            state "down", label: "Down", action: "moveDown", icon: "st.thermostat.thermostat-down", backgroundColor: "#FFFFFF", nextState: "downish"
            state "downish", label: "Down", action: "", icon: "st.thermostat.thermostat-down", backgroundColor: "#00FF00", nextState: "down"
        }
        standardTile("reboot", "device.reboot", width: 2, height: 2, canChangeIcon: false, canChangeBackground: false) {
            state "reboot", label: "Reboot", action: "rebootNow", icon: "st.Appliances.appliances17", backgroundColor: "#FFFFFF", nextState: "rebooting"
            state "rebooting", label: "...", action: "", icon: "st.Appliances.appliances17", backgroundColor: "#00FF00", nextState: "reboot"
        }
        standardTile("flipStatus", "device.flipStatus", width: 2, height: 2, canChangeIcon: false, decoration: "flat") {
            state "off", label: "Flip", action: "toggleFlip", icon: "http://smartthings.belgarion.s3.amazonaws.com/images/3D-Slider-OFF-Top.png", backgroundColor: "#FFFFFF", nextState: "..."
            state "on", label: "Flip", action: "toggleFlip", icon: "http://smartthings.belgarion.s3.amazonaws.com/images/3D-Slider-ON-Top.png", backgroundColor: "#FFFFFF", nextState: "..."
            state "...", label: "...", action: "", nextState: "..."
        }
        standardTile("mirrorStatus", "device.mirrorStatus", width: 2, height: 2, canChangeIcon: false, decoration: "flat") {
            state "off", label: "Mirror", action: "toggleMirror", icon: "http://smartthings.belgarion.s3.amazonaws.com/images/3D-Slider-OFF-Top.png", backgroundColor: "#FFFFFF", nextState: "..."
            state "on", label: "Mirror", action: "toggleMirror", icon: "http://smartthings.belgarion.s3.amazonaws.com/images/3D-Slider-ON-Top.png", backgroundColor: "#FFFFFF", nextState: "..."
            state "...", label: "...", action: "", nextState: "..."
        }
        standardTile("rotateStatus", "device.rotateStatus", width: 2, height: 2, canChangeIcon: false, decoration: "flat") {
            state "off", label: "Rotation", action: "changeRotation", icon: "http://smartthings.belgarion.s3.amazonaws.com/images/3D-Slider-OFF-Top.png", backgroundColor: "#FFFFFF", nextState: "..."
            state "cw", label: "90�", action: "changeRotation", icon: "http://smartthings.belgarion.s3.amazonaws.com/images/Rotate-CW.png", backgroundColor: "#FFFFFF", nextState: "..."
            state "ccw", label: "90�", action: "changeRotation", icon: "http://smartthings.belgarion.s3.amazonaws.com/images/Rotate-CCW.png", backgroundColor: "#FFFFFF", nextState: "..."
            state "...", label: "...", action: "", nextState: "..."
        }
        controlTile("levelSliderControlSpeed", "device.levelSpeed", "slider", height: 1, width: 3, inactiveLabel: false, range:"(1..8)") {
            state "speed", action: "setLevelSpeed"
        }
        standardTile("labelSpeed", "device.level", height: 1, width: 3, inactiveLabel: false) {
            state "speedLabel", label: "PTZ Speed", action: ""
        }
        controlTile("levelSliderControlSensitivity", "device.levelSensitivity", "slider", height: 1, width: 3, inactiveLabel: false, range:"(1..6)") {
            state "sensitivity", action: "setLevelSensitivity"
        }
        standardTile("labelSensitivity", "device.level", height: 1, width: 3, inactiveLabel: false) {
            state "sensitivityLabel", label: "Motion Sensitivity", action: ""
        }

        main "take"
        details(["take", "ledState", "motionStatus", "camDetails", "preset1", "preset2", "preset3", "preset4", "preset5", "preset6", "zoomOut", "up", "zoomIn", "left", "refresh", "right", "recState", "down", "reboot", "flipStatus", "mirrorStatus", "rotateStatus", "labelSpeed", "labelSensitivity", "levelSliderControlSpeed", "levelSliderControlSensitivity"])
    }
}

//*******************************  Commands  ***************************************

def take() {
    log.info "Taking Photo"
    // Set our image taking mode
    sendEvent(name: "hubactionMode", value: "s3", displayed: false)
    hubGetImage("/cgi-bin/snapshot.cgi")
}

def changeNvLED() {
    doDebug("changeNvLED -> hubGet Enabled?: ${doHubGet ?: false}")
    if (!state.stateNvStatus || (state.stateNvStatus == "off")) {
        log.info "Change NightVision: IR LED set to 'ON'"
        state.stateNvSwitch = 3
        state.stateNvStatus = "on"
        sendEvent(name: "ledStatus", value: "on", isStateChange: true, displayed: false)
    }
    else if (state.stateNvStatus == "on") {
        log.info "Change NightVision: IR LED set to 'AUTO'"
        state.stateNvSwitch = 4
        state.stateNvStatus = "auto"
        sendEvent(name: "ledStatus", value: "auto", isStateChange: true, displayed: false)
    }
    else {
        log.info "Change NightVision: IR LED set to 'OFF'"
        state.stateNvSwitch = 0
        state.stateNvStatus = "off"
        sendEvent(name: "ledStatus", value: "off", isStateChange: true, displayed: false)
    }
    String apiCommand = setFlipMirrorMotionRotateNv()
    hubGet(apiCommand)
}

def changeRecord() {
    doDebug("changeRecord -> hubGet Enabled?: ${doHubGet ?: false}")
    if (!state.stateRecord || (state.stateRecord == "off")) {
        log.info "Change Record: Recording set to 'ON'"
        state.stateRecSwitch = 1
        state.stateRecord = "on"
        sendEvent(name: "recStatus", value: "on", isStateChange: true, displayed: false)
    }
    else if (state.stateRecord == "on") {
        log.info "Change Record: Recording set to 'AUTO'"
        state.stateRecSwitch = 0
        state.stateRecord = "auto"
        sendEvent(name: "recStatus", value: "auto", isStateChange: true, displayed: false)
    }
    else {
        log.info "Change Record: Recording set to 'OFF'"
        state.stateRecSwitch = 2
        state.stateRecord = "off"
        sendEvent(name: "recStatus", value: "off", isStateChange: true, displayed: false)
    }
    String apiCommand = setFlipMirrorMotionRotateNv()
    hubGet(apiCommand)
}

def changeRotation() {
    doToggleRotation(true)
}

def moveDown() {
    log.info "Panning Down"
    sendEvent(name: "down", value: "down", isStateChange: true, displayed: false)
    delayBetween([doMoveCmd("start", "Down", "0", "${device.currentValue('levelSpeed') ?: 1}", "0", "0"), doMoveCmd("stop", "Down", "0", "${device.currentValue('levelSpeed') ?: 1}", "0", "0")], msDelay())
}

def moveLeft() {
    log.info "Panning Left"
    sendEvent(name: "left", value: "left", isStateChange: true, displayed: false)
    delayBetween([doMoveCmd("start", "Left", "0", "${device.currentValue('levelSpeed') ?: 1}", "0", "0"), doMoveCmd("stop", "Left", "0", "${device.currentValue('levelSpeed') ?: 1}", "0", "0")], msDelay())
}

def moveRight() {
    log.info "Panning Right"
    sendEvent(name: "right", value: "right", isStateChange: true, displayed: false)
    delayBetween([doMoveCmd("start", "Right", "0", "${device.currentValue('levelSpeed') ?: 1}", "0", "0"), doMoveCmd("stop", "Right", "0", "${device.currentValue('levelSpeed') ?: 1}", "0", "0")], msDelay())
}

def moveUp() {
    log.info "Panning Up"
    sendEvent(name: "up", value: "up", isStateChange: true, displayed: false)
    delayBetween([doMoveCmd("start", "Up", "0", "${device.currentValue('levelSpeed') ?: 1}", "0", "0"), doMoveCmd("stop", "Up", "0", "${device.currentValue('levelSpeed') ?: 1}", "0", "0")], msDelay())
}

def poll() {  // Polling capability: this command will be called approximately every 5 minutes to check the device's state
    log.trace "Poll"
    doDebug("poll -> BEGIN")
    sendEvent(name: "hubactionMode", value: "local", displayed: false)

    def cmds = []  // Build our commands list
    String apiCommand = setFlipMirrorMotionRotateNv()  // Get the commands to set the Flip/Mirror/Motion/NightVision/Rotate camera states

    cmds << hubGet("/cgi-bin/configManager.cgi?action=getConfig&name=VideoInOptions&name=MotionDetect")  // Current Flip, Mirroring, Motion Dectection, Rotate90 & Night Vision settings
    cmds << hubGet(apiCommand)  // Send the commands

    //doDebug("poll -> Executing cmds: ${cmds.inspect()}")
    delayBetween(cmds, msDelay())
}

def presetCmd(presetNum) {
    log.info "Moving to Preset # ${presetNum}"
    sendEvent(name: "presetStatus", value: "", isStateChange: true, displayed: false)
    hubGet("/cgi-bin/ptz.cgi?action=start&channel=0&code=GotoPreset&arg1=0&arg2=${presetNum}&arg3=0&arg4=0")
}

def presetCmd1() {
    presetCmd(1)
}

def presetCmd2() {
    presetCmd(2)
}

def presetCmd3() {
    presetCmd(3)
}

def presetCmd4() {
    presetCmd(4)
}

def presetCmd5() {
    presetCmd(5)
}

def presetCmd6() {
    presetCmd(6)
}

def rebootNow() {
    log.info "Rebooting..."
    sendEvent(name: "reboot", value: "reboot", isStateChange: true, displayed: false)
    hubGet("/cgi-bin/magicBox.cgi?action=reboot")
}

def refresh() {
    log.info "Refreshing Values..."
    poll()
}

def setLevelSpeed(int value) {
    log.info "Adjusting PTZ speed..."
    def oldSpeed = device.currentValue('levelSpeed')
    doDebug("setLevel -> PTZ Speed changed from '${device.currentValue('levelSpeed') ?: 'Default:1'}' to '$value'")
    sendEvent(name: "levelSpeed", value: value, isStateChange: true, displayed: false)
}

def setLevelSensitivity(int value) {
    log.info "Adjusting Motion Detect Sensitivity..."
    def oldSensitivity = device.currentValue('levelSensitivity')
    state.stateMotionSensitivity = value
    doDebug("setLevel -> Motion Detect Sensitivity changed from '${device.currentValue('levelSensitivity') ?: 'Default:1'}' to '$state.stateMotionSensitivity'")
    sendEvent(name: "levelSensitivity", value: state.stateMotionSensitivity, isStateChange: true, displayed: false)
    doToggleMotionSensitivity(true)
}

def toggleFlip() {
    doToggleFlip(true)
}

def toggleMirror() {
    doToggleMirror(true)
}

def toggleMotion() {
    doToggleMotion(true)
}

def zoomIn() {
    log.info "Zooming In"
    sendEvent(name: "zoomIn", value: "", isStateChange: true, displayed: false)
    delayBetween([doMoveCmd("start", "ZoomTele", "0", "${device.currentValue('levelSpeed') ?: 1}", "0", "0"), doMoveCmd("stop", "ZoomTele", "0", "${device.currentValue('levelSpeed') ?: 1}", "0", "0")], msDelay())
}

def zoomOut() {
    log.info "Zooming Out"
    sendEvent(name: "zoomOut", value: "", isStateChange: true, displayed: false)
    delayBetween([doMoveCmd("start", "ZoomWide", "0", "${device.currentValue('levelSpeed') ?: 1}", "0", "0"), doMoveCmd("stop", "ZoomWide", "0", "${device.currentValue('levelSpeed') ?: 1}", "0", "0")], msDelay())
}

def configure() {
    doDebug("'configure()' called...")
}

def initialize() {
    doDebug("'initialize()' called...")
}

def updated() {
    doDebug("'updated()' called...")
}

//*******************************  Private Commands  *******************************

private doMoveCmd(String action, String motion, String argOne, String argTwo, String argThree, String argFour) {
    def apiCommand = "/cgi-bin/ptz.cgi?action=${action}&channel=0&code=${motion}&arg1=${argOne}&arg2=${argTwo}&arg3=${argThree}&arg4=${argFour}"
    hubGet(apiCommand)
}

private doToggleFlip(Boolean doHubGet) {
    doDebug("doToggleFlip -> BEGIN (hubGet Enabled?: ${doHubGet ?: false})")
    if (doHubGet == true) {
        if (!state.stateFlip || (state.stateFlip == "off")) {
            log.info "Toggle Image Flip: Turning ON"
            state.stateFlip = "on"
            sendEvent(name: "flipStatus", value: "on", isStateChange: true, displayed: false)
        }
        else {
            log.info "Toggle Image Flip: Turning OFF"
            state.stateFlip = "off"
            sendEvent(name: "flipStatus", value: "off", isStateChange: true, displayed: false)
        }
        String apiCommand = setFlipMirrorMotionRotateNv()
        hubGet(apiCommand)
    }
    else {
        if (flipStatus == "off" && (device.currentValue("flipStatus") == "on")) {
            log.info "Toggle Image Flip: Turning ON"
        }
        else if (flipStatus == "on" && (device.currentValue("flipStatus") == "off")) {
            log.info "Toggle Image Flip: Turning OFF"
        }
    }
}

private doToggleMirror(Boolean doHubGet) {
    doDebug("doToggleMirror -> BEGIN (hubGet Enabled?: ${doHubGet ?: false})")
    if (doHubGet == true) {
        if (!state.stateMirror || (state.stateMirror == "off")) {
            log.info "Toggle Image Mirroring: Turning ON"
            state.stateMirror = "on"
            sendEvent(name: "mirrorStatus", value: "on", isStateChange: true, displayed: false)
        }
        else {
            log.info "Toggle Image Mirroring: Turning OFF"
            state.stateMirror = "off"
            sendEvent(name: "mirrorStatus", value: "off", isStateChange: true, displayed: false)
        }
        String apiCommand = setFlipMirrorMotionRotateNv()
        hubGet(apiCommand)
    }
    else {
        if (mirrorStatus == "off" && (device.currentValue("mirrorStatus") == "on")) {
            log.info "Toggle Image Mirroring: Turning ON"
        }
        else if (mirrorStatus == "on" && (device.currentValue("mirrorStatus") == "off")) {
            log.info "Toggle Image Mirroring: Turning OFF"
        }
    }
}

private doToggleMotion(Boolean doHubGet) {
    doDebug("doToggleMotion -> BEGIN (hubGet Enabled?: ${doHubGet ?: false})")
    if (doHubGet == true) {
        if (!state.stateMotion || (state.stateMotion == "off")) {
            log.info "Toggle Motion: Turning 'Motion Sensor' ON"
            state.stateMotion = "on"
            sendEvent(name: "motionStatus", value: "on", isStateChange: true, displayed: false)
        }
        else {
            log.info "Toggle Motion: Turning 'Motion Sensor' OFF"
            state.stateMotion = "off"
            sendEvent(name: "motionStatus", value: "off", isStateChange: true, displayed: false)
        }
        String apiCommand = setFlipMirrorMotionRotateNv()
        hubGet(apiCommand)
    }
    else {
        if (motionStatus == "off" && (device.currentValue("motionStatus") == "on")) {
            log.info "Toggle Motion: Turning 'Motion Sensor' ON"
        }
        else if (motionStatus == "on" && (device.currentValue("motionStatus") == "off")) {
            log.info "Toggle Motion: Turning 'Motion Sensor' OFF"
        }
    }
}

private doToggleMotionSensitivity(Boolean doHubGet) {
    doDebug("doToggleMotionSensitivity -> BEGIN (hubGet Enabled?: ${doHubGet ?: false})")
    if (doHubGet == true) {
        if (!state.stateMotionSensitivity) {
            log.info "Setting Motion Sensitivity: Level set to 1"
            state.stateMotionSensitivity = 1
        }
        else {
            log.info "Setting Motion Sensitivity: Level set to $state.stateMotionSensitivity"
        }
        String apiCommand = setFlipMirrorMotionRotateNv()
        hubGet(apiCommand)
    }
}

private doToggleRotation(Boolean doHubGet) {
    doDebug("doToggleRotation -> BEGIN (hubGet Enabled?: ${doHubGet ?: false})")
    if (doHubGet == true) {
        if (!state.stateRotation || (state.stateRotation == "off")) {
            log.info "Toggle 90� Rotation: Rotation set to 'Clockwise'"
            state.stateRotate = 1
            state.stateRotation = "cw"
            sendEvent(name: "rotateStatus", value: "cw", isStateChange: true, displayed: false)
        }
        else if (state.stateRotation == "cw") {
            log.info "Toggle 90� Rotation: Rotation set to 'Counter-Clockwise'"
            state.stateRotate = 2
            state.stateRotation = "ccw"
            sendEvent(name: "rotateStatus", value: "ccw", isStateChange: true, displayed: false)
        }
        else {
            log.info "Toggle 90� Rotation: Rotation turned 'OFF'"
            state.stateRotate = 0
            state.stateRotation = "off"
            sendEvent(name: "rotateStatus", value: "off", isStateChange: true, displayed: false)
        }
        String apiCommand = setFlipMirrorMotionRotateNv()
        hubGet(apiCommand)
    }
    else {
        if (rotateStatus == "off" && (state.stateRotation == "cw")) {
            log.info "Toggle 90� Rotation: Rotation set to 'Clockwise'"
        }
        else if (rotateStatus == "cw" && (state.stateRotation == "ccw")) {
            log.info "Toggle 90� Rotation: Rotation set to 'Counter-Clockwise'"
        }
        else if (rotateStatus == "ccw" && (state.stateRotation == "off")) {
            log.info "Toggle 90� Rotation: Rotation turned 'OFF'"
        }
    }
}

private String setFlipMirrorMotionRotateNv() {  // Return the string of commands needed to set the Flip/Mirror/Motion/NightVision/Rotate camera states
    doDebug("setFlipMirrorMotionRotateNv -> Current: flipStatus = $state.stateFlip, mirrorStatus = $state.stateMirror, motionStatus = $state.stateMotion, nvStatus = $state.stateNvStatus, recordStatus = $state.stateRecord, rotateStatus = $state.stateRotation (movement speed = ${device.currentValue('levelSpeed') ?: 'Default:1'}, motion sensitivity = ${state.stateMotionSensitivity ?: 'Default:1'})")

    // Until I turn this into a Parent -> Child relationship, make sure all of the state values exist
    if (!state.stateFlip) { state.stateFlip = "off"}
    if (!state.stateMirror) { state.stateMirror = "off"}
    if (!state.stateMotion) { state.stateMotion = "off"}
    if (!state.stateMotionSensitivity) { state.stateMotionSensitivity = 1 }
    if (!state.stateNvStatus) { state.stateNvStatus = "off"}
    if (!state.stateNvSwitch) { state.stateNvSwitch = 0}
    if (!state.stateRecord) { state.stateRecord = "off"}
    if (!state.stateRecSwitch) { state.stateRecSwitch = 2}
    if (!state.stateRotate) { state.stateRotate = 0}
    if (!state.stateRotation) { state.stateRotation = "off"}

    String apiCommand = "/cgi-bin/configManager.cgi?action=setConfig" +
                        "&MotionDetect[$camChannel].Enable=${state.stateMotion == 'off' ? false : true}" +
                        "&MotionDetect[$camChannel].Level=$state.stateMotionSensitivity" +
                        "&RecordMode[$camChannel].Mode=$state.stateRecSwitch" +
                        "&VideoInOptions[$camChannel].Flip=${state.stateFlip == 'off' ? false : true}" +
                        "&VideoInOptions[$camChannel].NightOptions.Flip=${state.stateFlip == 'off' ? false : true}" +
                        "&VideoInOptions[$camChannel].Mirror=${state.stateMirror == 'off' ? false : true}" +
                        "&VideoInOptions[$camChannel].NightOptions.Mirror=${state.stateMirror == 'off' ? false : true}" +
                        "&VideoInOptions[$camChannel].NightOptions.SwitchMode=$state.stateNvSwitch" +
                        "&VideoInOptions[$camChannel].Rotate90=$state.stateRotate" +
                        "&VideoInOptions[$camChannel].NightOptions.Rotate90=$state.stateRotate" +
                        "&VideoInOptions[$camChannel].NightOptions.SwitchMode=$state.stateNvSwitch"

}

//*******************************  Network Commands  *******************************

private hubGet(def apiCommand, Boolean isImage = false) {  // Called for all non-Image requests and Local IP Image requests
    doDebug("hubGet -> BEGIN")
    doDebug("hubGet -> apiCommand = $apiCommand (size = ${apiCommand.size()})")

    // Make sure we have an IP
    if (isIpAddress(camIP) != true) {
        state.stateHost = convertHostnameToIPAddress(camIP)
        doDebug("hubGet -> Host name '$camIP' resolved to IP '$state.stateHost'")
    }
    else {
        state.stateHost = camIP
        doDebug("hubGet -> Using IP '$state.stateHost'")
    }

    // Set the Network Device Id
    def hosthex = convertIPtoHex(state.stateHost).toUpperCase()
    def porthex = convertPortToHex(camPort).toUpperCase()
    device.deviceNetworkId = "$hosthex:$porthex"
    doDebug("hubGet -> Network Device Id = $device.deviceNetworkId")

    // Set our Headers
    def headers = [:]
    def userPassAscii = "${camUser}:${camPassword}"
    def userPass = "Basic " + userPassAscii.encodeAsBase64().toString()
    headers.put("HOST", "${state.stateHost}:${camPort}")
    headers.put("Authorization", userPass)
    doDebug("hubGet -> headers = ${headers.inspect()}")

    // Do the deed
    try {
        def hubAction = new physicalgraph.device.HubAction(
            method: "GET",
            path: apiCommand,
            headers: headers
        )
        if (isImage) {
            hubAction.options = [outputMsgToS3:true]
        }
        else {
            hubAction.options = [outputMsgToS3:false]
        }
        doDebug("hubGet -> hubAction = $hubAction")
        hubAction
    }
    catch (Exception e) {
        log.warn "hubGet -> 'HubAction' Exception -> $e ($hubAction)"
    }
}

private hubGetImage(def apiCommand) {  // Called when taking a picture
    doDebug("hubGetImage -> BEGIN")
    doDebug("apiCommand = $apiCommand")

    // Make sure we have an IP
    if (isIpAddress(camIP) != true) {
        state.stateHost = convertHostnameToIPAddress(camIP)
        doDebug("hubGetImage -> Host name '$camIP' resolved to IP '$state.stateHost'")
    }
    else {
        state.stateHost = camIP
        doDebug("hubGetImage -> Using IP '$state.stateHost'")
    }

    // If this is a local IP, use HubAction
    if (ipIsLocal(state.stateHost)) {
        doDebug("hubGetImage -> Local IP detected: Switching to HubAction")
        def hubAction = hubGet(apiCommand, true)
        hubAction
    }
    else {
        // Set the Network Device Id
        def hosthex = convertIPtoHex(state.stateHost).toUpperCase()
        def porthex = convertPortToHex(camPort).toUpperCase()
        device.deviceNetworkId = "$hosthex:$porthex"
        doDebug("hubGetImage -> Network Device Id = $device.deviceNetworkId")

        // Set our Params & Headers
        def headers = [:]
        def userPassAscii = "${camUser}:${camPassword}"
        def userPass = "Basic " + userPassAscii.encodeAsBase64().toString()
        headers.put("Authorization", userPass)
        doDebug("hubGetImage -> headers = ${headers.inspect()}")

        def params = [
            uri: "http://${state.stateHost}:${camPort}",
            path: apiCommand,
            headers: headers
        ]

        // Do the deed
        try {
            httpGet(params) { response ->
                response.headers.each {
                    doDebug("hubGetImage -> httpGet response for ${it.name} = ${it.value}")
                }
                doDebug("hubGetImage -> response contentType: ${response.contentType}")
                parseHttpGetResponse(response)
            }
        }
        catch (Exception e) {
            log.warn "hubGetImage -> 'httpGet' Exception -> $e"
        }
    }
}

//*******************************  Process Responses  ******************************

def parse(String description) {  // 'HubAction' Method: Parse events into attributes or save an image (used with a public IP address)
    doDebug("parse -> BEGIN")

    def descMap = parseDescriptionAsMap(description)
    def retResult = []

    doDebug("parse -> descMap RAW = ${descMap.inspect()}")

    if (descMap["body"]) {
        def body = new String(descMap["body"].decodeBase64())
        doDebug("parse -> Body size = ${body.size()}")
    }
    if (descMap["headers"]) {
        String headers = new String(descMap["headers"].decodeBase64())
        doDebug("parse -> headers = ${headers}")
    }

    // Image Response
    if (descMap["bucket"] && descMap["key"]) {
        doDebug("parse -> Detected: S3 image response")
        retResult = putImageInS3(descMap)
    }
    else if (descMap["headers"].contains("image/jpeg")) {
        doDebug("parse -> Detected: S3 image response")
        retResult = putImageInS3(descMap)
    }
    // Non-Image Response
    else if (descMap["headers"] && descMap["body"]) {
        doDebug("parse -> Detected: Non-image response")
        def bodyVal = new String(descMap["body"].decodeBase64())
        retResult = processResponse(bodyVal)
    }
    else {
        doDebug("parse -> Detected: Empty body response")
    }
    doDebug("parse -> END")
    return retResult
}

private parseDescriptionAsMap(description) {
    def map = [:]
    for(String keyValue : description.split(" *, *")) {
        String[] pairs = keyValue.split(" *: *", 2);
        map.put(pairs[0], pairs.length == 1 ? "" : pairs[1]);
    }
    return map
}

def parseHttpGetResponse(response) {  // 'httpGet' Method: Parse events into attributes or save an image (used with a public IP address)
    doDebug("parseHttpGetResponse -> BEGIN")
    doDebug("parseHttpGetResponse -> headers = ${response.headers.'Content-Type'}, status = $response.status")

    def retResult = []

    if (response.status == 200) {
        if (response.headers.'Content-Type'.contains("image/jpeg")) {  // Image Response
            if (response.data) {
                def image = response.data
                def bytes = image.buf

                // Broadcast our image file data (thanks to RBoy for the imageDataJpeg saving)
                String str = bytes.encodeBase64()
                sendEvent(name: "imageDataJpeg", value: str, isStateChange: true, displayed: false)

                def picName = getPictureName()
                log.info "parseHttpGetResponse -> Saving image '$picName' to the SmartThings cloud"
                storeImage(picName, image)  // Removes the data from the 'image' object
            }
            else {
                log.warn "Received an empty response from camera, expecting a JPEG image"
            }
        }
        else { // Non-Image Response
            def body = response.data.getText()
            retResult = processResponse(body)
        }
    }
    else { // Otherwise process the camera response codes
        log.warn "Error response from ${state.stateHost}:${camPort}, HTTP Response code = $response.status"
    }
    doDebug("parseHttpGetResponse -> END")
    return retResult
}

//*******************************  Image Handling  *********************************

def putImageInS3(map) {
    doDebug("putImageInS3 -> BEGIN")
    try {
        def imageBytes = getS3Object(map.bucket, map.key + ".jpg")
        doDebug("putImageInS3 -> S3Object = $imageBytes")

        if (imageBytes) {
            def s3ObjectContent = imageBytes.getObjectContent()
            def image = new ByteArrayInputStream(s3ObjectContent.bytes)
            if (image) {
                def bytes = image.buf

                // Broadcast our image file data (thanks to RBoy for the imageDataJpeg saving)
                String str = bytes.encodeBase64()
                sendEvent(name: "imageDataJpeg", value: str, isStateChange: true, displayed: false)

                def picName = getPictureName()
                log.info "putImageInS3 -> Saving image '$picName' to the SmartThings cloud"
                storeImage(picName, image)  // Removes the data from the 'image' object
            }
            else {
                log.warn "putImageInS3 -> Received an empty response from camera, expecting a JPEG image"
            }
        }
        else {
            log.warn "putImageInS3 -> Received an empty response from camera, expecting a JPEG image"
        }
    }
    catch(Exception e) {
        log.error "putImageInS3 -> Exeception thrown: $e"
    }
    finally {
        //Explicitly close the stream
        if (s3ObjectContent) { s3ObjectContent.close() }
    }
    doDebug("putImageInS3 -> END")
}

private getPictureName() {
    def pictureUuid = java.util.UUID.randomUUID().toString().replaceAll('-', '').toUpperCase()
    "Amcrest" + "_$pictureUuid" + ".jpg"
}

//******************************  Parse HubAction/httpGet/Polling Responses  *******************************

// Process any non-image response (used for both local HubAction and public httpGet requests)
private processResponse(def bodyIn) {
    doDebug("processResponse -> BEGIN")

    def retResult = []

    String body = new String(bodyIn.toString())
    body = body.replaceAll("\\r\\n|\\r|\\n", " ")
    doDebug("processResponse -> Body size = ${body.size()}")

    try {
        // Check for errors
        if (body.find("401 Unauthorized")) {
            log.warn "processResponse -> END -> Camera responded with a 401 Unauthorized error: Error = ${body}"
            return retResult
        }
        if (body.find("404 Not Found")) {
            log.warn "processResponse -> END -> Camera responded with a 404 Not Found error: Error = ${body}"
            return retResult
        }

        // Check for a single word result
        if (body.find("OK")) {
            doDebug("processResponse -> END -> Command successful")
            return retResult
        }
        else if (body.find("ERROR")) {
            log.warn "processResponse -> END -> Command failed"
            return retResult
        }

        // Flip
        if (body.contains("VideoInOptions[$camChannel].Flip=false") && (state.stateFlip != "off")) {
            log.trace "processResponse -> Turning Flip Image 'OFF'"
            state.stateFlip = "off"
            sendEvent(name: "flipStatus", value: "off", isStateChange: true, displayed: false)
        }
        else if (body.contains("VideoInOptions[$camChannel].Flip=true") && (state.stateFlip != "on")) {
            log.trace "processResponse -> Turning Flip Image 'ON'"
            state.stateFlip = "on"
            sendEvent(name: "flipStatus", value: "on", isStateChange: true, displayed: false)
        }

        // Mirror
        if (body.contains("VideoInOptions[$camChannel].Mirror=false") && (state.stateMirror != "off")) {
            log.trace "processResponse -> Turning Mirror Image 'OFF'"
            state.stateMirror = "off"
            sendEvent(name: "mirrorStatus", value: "off", isStateChange: true, displayed: false)
        }
        else if (body.contains("VideoInOptions[$camChannel].Mirror=true") && (state.stateMirror != "on")) {
            log.trace "processResponse -> Turning Mirror Image 'ON'"
            state.stateMirror = "on"
            sendEvent(name: "mirrorStatus", value: "on", isStateChange: true, displayed: false)
        }

        // Motion Detection
        if (body.contains("MotionDetect[$camChannel].Enable=false") && (state.stateMotion != "off")) {
            log.trace "processResponse -> Turning Motion Sensor 'OFF'"
            state.stateMotion = "off"
            sendEvent(name: "motionStatus", value: "off", isStateChange: true, displayed: false)
        }
        else if (body.contains("MotionDetect[$camChannel].Enable=true") && (state.stateMotion != "on")) {
            log.trace "processResponse -> Turning Motion Sensor 'ON'"
            state.stateMotion = "on"
            sendEvent(name: "motionStatus", value: "on", isStateChange: true, displayed: false)
        }

        // Motion Sensitivity
        if (body.contains("MotionDetect[$camChannel].Level=1")) {
            log.trace "processResponse -> Setting Motion Sensitivity to '1' (Low)"
            sendEvent(name: "levelSensitivity", value: 1, isStateChange: true, displayed: false)
        }
        else if (body.contains("MotionDetect[$camChannel].Level=2")) {
            log.trace "processResponse -> Setting Motion Sensitivity to '2' (Medium-Low)"
            sendEvent(name: "levelSensitivity", value: 2, isStateChange: true, displayed: false)
        }
        else if (body.contains("MotionDetect[$camChannel].Level=3")) {
            log.trace "processResponse -> Setting Motion Sensitivity to '3' (Medium)"
            sendEvent(name: "levelSensitivity", value: 3, isStateChange: true, displayed: false)
        }
        else if (body.contains("MotionDetect[$camChannel].Level=4")) {
            log.trace "processResponse -> Setting Motion Sensitivity to '4' (Medium-High)"
            sendEvent(name: "levelSensitivity", value: 4, isStateChange: true, displayed: false)
        }
        else if (body.contains("MotionDetect[$camChannel].Level=5")) {
            log.trace "processResponse -> Setting Motion Sensitivity to '5' (High)"
            sendEvent(name: "levelSensitivity", value: 5, isStateChange: true, displayed: false)
        }
        else if (body.contains("MotionDetect[$camChannel].Level=6")) {
            log.trace "processResponse -> Setting Motion Sensitivity to '6' (Highest)"
            sendEvent(name: "levelSensitivity", value: 6, isStateChange: true, displayed: false)
        }

        // Rotation
        if (body.contains("VideoInOptions[$camChannel].Rotate90=0") && (state.stateRotate != 0)) {
            log.trace "processResponse -> Setting Rotation to 0�"
            state.stateRotate = 0
            state.stateRotation = "off"
            sendEvent(name: "rotateStatus", value: "off", isStateChange: true, displayed: false)
        }
        else if (body.contains("VideoInOptions[$camChannel].Rotate90=1") && (state.stateRotate != 1)) {
            log.trace "processResponse -> Setting Rotation to 90�"
            state.stateRotate = 1
            state.stateRotation = "cw"
            sendEvent(name: "rotateStatus", value: "cw", isStateChange: true, displayed: false)
        }
        else if (body.contains("VideoInOptions[$camChannel].Rotate90=2") && (state.stateRotate != 2)) {
            log.trace "processResponse -> Setting Rotation to 270�"
            state.stateRotate = 2
            state.stateRotation = "ccw"
            sendEvent(name: "rotateStatus", value: "ccw", isStateChange: true, displayed: false)
        }

        // NightVision
        if (body.contains("VideoInOptions[$camChannel].NightOptions.SwitchMode=0") && (state.stateNvSwitch != 0)) {
            log.trace "processResponse -> Turning Night Vision 'OFF'"
            state.stateNvSwitch = 0
            state.stateNvStatus = "off"
            sendEvent(name: "ledStatus", value: "off", isStateChange: true, displayed: false)
        }
        else if (body.contains("VideoInOptions[$camChannel].NightOptions.SwitchMode=3") && (state.stateNvSwitch != 3)) {
            log.trace "processResponse -> Turning Night Vision 'ON'"
            state.stateNvSwitch = 3
            state.stateNvStatus = "on"
            sendEvent(name: "ledStatus", value: "on", isStateChange: true, displayed: false)
        }
        else if (body.contains("VideoInOptions[$camChannel].NightOptions.SwitchMode=4") && (state.stateNvSwitch != 4)) {
            log.trace "processResponse -> Turning Night Vision to 'AUTO'"
            state.stateNvSwitch = 4
            state.stateNvStatus = "auto"
            sendEvent(name: "ledStatus", value: "auto", isStateChange: true, displayed: false)
        }

        // Record
        if (body.contains("RecordMode[$camChannel].Mode=2") && (state.stateRecSwitch != 2)) {
            log.trace "processResponse -> Turning Night Vision 'OFF'"
            state.stateRecSwitch = 2
            state.stateRecord = "off"
            sendEvent(name: "recStatus", value: "off", isStateChange: true, displayed: false)
        }
        else if (body.contains("RecordMode[$camChannel].Mode=1") && (state.stateRecSwitch != 1)) {
            log.trace "processResponse -> Turning Night Vision 'ON'"
            state.stateRecSwitch = 1
            state.stateRecord = "on"
            sendEvent(name: "recStatus", value: "on", isStateChange: true, displayed: false)
        }
        else if (body.contains("RecordMode[$camChannel].Mode=0") && (state.stateRecSwitch != 0)) {
            log.trace "processResponse -> Turning Night Vision to 'AUTO'"
            state.stateRecSwitch = 0
            state.stateRecord = "auto"
            sendEvent(name: "recStatus", value: "auto", isStateChange: true, displayed: false)
        }
    }
    catch (Exception e) {
        log.warn "processResponse -> Exception thrown: $e"
    }
    doDebug("processResponse -> New: flipStatus = $state.stateFlip, mirrorStatus = $state.stateMirror, motionStatus = $state.stateMotion, nvStatus = $state.stateNvStatus, recordStatus = $state.stateRecord, rotateStatus = $state.stateRotation (movement speed = ${device.currentValue('levelSpeed') ?: 'Default:1'}, motion sensitivity = ${state.stateMotionSensitivity ?: 'Default:1'})")
    doDebug("processResponse -> END")
    return retResult
}

//*******************************  IP & Port Related  ******************************

private Integer convertHexToInt(hex) {
    Integer.parseInt(hex,16)
}

private String convertHexToIP(hex) {
    doDebug("convertHexToIP -> Convert hex to ip = $hex")
    [convertHexToInt(hex[0..1]),convertHexToInt(hex[2..3]),convertHexToInt(hex[4..5]),convertHexToInt(hex[6..7])].join(".")
}

private String convertHostnameToIPAddress(hostname) { // thanks go to cosmicpuppy and RBoy!
    def params = [
                  uri: "http://api.myiponline.net/dig?url=" + hostname
                 ]
    def retVal = null
    try {
        retVal = httpGet(params) { response ->
                    doDebug("convertHostnameToIPAddress -> Request was successful, data = $response.data, status=$response.status")
                    for(result in response.data) {
                        for(subresult in result) {
                            if (subresult.type == "A") {
                                return subresult.ip
                            }
                        }
                    }
        }
    }
    catch (Exception e) {
        log.warn "Unable to convert hostname to IP Address, Error: $e"
    }
    return retVal
}

private String convertIPtoBinary(ipAddress) {
    try {
        def bin = ""
        def oct = ""
        ipAddress.tokenize( '.' ).collect {
            oct = String.format( '%8s', Integer.toString(it.toInteger(), 2) ).replace(' ', '0')
            bin = bin + oct
        }
        doDebug("convertIPtoBinary -> IP address passed in is $ipAddress and the converted binary is $bin")
        return bin
    }
    catch ( Exception e ) {
        log.error "IP Address is invalid ($ipAddress), Error: $e"
        return // Nothing to return
    }
}

private String convertIPtoHex(ipAddress) {
    try {
        String hex = ipAddress.tokenize( '.' ).collect {  String.format( '%02x', it.toInteger() ) }.join()
        doDebug("convertIPtoHex -> IP address passed in is $ipAddress and the converted hex code is $hex")
        return hex
    }
    catch ( Exception e ) {
        log.error "IP Address is invalid ($ipAddress), Error: $e"
        return //Nothing to return
    }
}

private String convertPortToHex(port) {
    try {
        String hexport = port.toString().format( '%04x', port.toInteger() )
        doDebug("convertPortToHex -> Port passed in is $port and the converted hex code is $hexport")
        return hexport
    }
    catch ( Exception e ) {
        log.error "Port is invalid ($ipAddress), Error: $e"
        return //Nothing to return
    }
}

private Boolean isIpAddress(String str) {
    // See: http://stackoverflow.com/questions/18157217/how-can-i-check-if-a-string-is-an-ip-in-groovy
    try {
        String[] parts = str.split("\\.")
        if (parts.length != 4) {
            return false
        }
        for (int i = 0; i < 4; ++i) {
            int p = Integer.parseInt(parts[i])
            if (p > 255 || p < 0) {
                return false
            }
        }
        return true
    }
    catch ( Exception e ) {
        log.error "Unable to determine if IP Address is valid ($str), Error: $e"
        return false
    }
}

private Boolean ipIsLocal(ipAddress) {
    List ipPrivateRanges = ["00000000",          // 'LOCAL IP',  # 0/8
                            "00001010",          // 'LOCAL IP',  # 10/8
                            "01111111",          // 'LOCAL IP',  # 127.0/8
                            "1010100111111110",  // 'LOCAL IP',  # 169.254/16
                            "101011000001",      // 'LOCAL IP',  # 172.16/12
                            "1100000010101000"]  // 'LOCAL IP',  # 192.168/16
    def size = 17
    Boolean localAns = false
    try {
        def ipBinary = convertIPtoBinary(ipAddress)
        ipBinary = ipBinary.take(size)

        while (size-- > 7) {
            if (ipPrivateRanges.contains(ipBinary)) {
                doDebug("ipIsLocal -> Found = $ipBinary")
                localAns = true
                break
            }
            ipBinary = ipBinary.take(size)
        }
        doDebug("ipIsLocal -> Host IP '$ipAddress' is ${localAns ? 'Local' : 'Public' }")
        return localAns
    }
    catch (Exception e) {
        log.error "Exception: $e"
        return false
    }
    return localAns
}

private Integer msDelay() {
    return 500
}

//***********************************  Debugging  **********************************

private doDebug(Object... dbgStr) {
    if (camDebug) {
        log.debug dbgStr
    }
}
