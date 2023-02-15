#version 100

uniform float uMaxZaxis;//0
uniform vec2 uScale;//1
uniform float u_Progress;//4
uniform vec2 u_Resolution;//5
uniform float u_Right;
attribute vec4 vPosition;

varying float maxZaxis;
varying vec2 scale;
varying float progress;
varying vec2 resolution;
varying float right;

void main() {
    gl_Position = vPosition;
    maxZaxis = uMaxZaxis;
    scale = uScale;
    progress = u_Progress;
    resolution = u_Resolution;
    right = u_Right;
}