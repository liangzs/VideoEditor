#version 100

uniform vec2 uScale;//1
uniform float u_Time;//3
uniform float u_Progress;//4
uniform vec2 u_Resolution;//5
attribute vec4 vPosition;

varying vec2 scale;
varying float time;
varying float progress;
varying vec2 resolution;

void main() {
    gl_Position = vPosition;
    scale = uScale;
    time = u_Time;
    progress = u_Progress;
    resolution = u_Resolution;
}