#version 100

uniform vec2 u_Resolution;
uniform vec2 u_Center;
uniform vec3 u_BaseColor;
uniform float u_Progress;
attribute vec4 vPosition;

varying vec2 resolution;
varying vec2 center;
varying vec3 color;
varying float progress;

void main() {
    gl_Position = vPosition;
    resolution = u_Resolution;
    center = u_Center;
    color = u_BaseColor;
    progress = u_Progress;
}