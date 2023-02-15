#version 100
attribute vec4 vPosition;
attribute vec2 vCoord;
uniform mat4 vMatrix;
uniform float alpha;
varying float vAlpha;

varying vec2 textureCoordinate;

void main(){
    gl_Position = vMatrix*vPosition;
    textureCoordinate = vCoord;
    vAlpha=alpha;
}