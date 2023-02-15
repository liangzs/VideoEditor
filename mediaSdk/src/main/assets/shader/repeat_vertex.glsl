#version 100
attribute vec4 vPosition;
attribute vec2 vCoord;
attribute float phase;
uniform mat4 vMatrix;
uniform float alpha;

varying float vAlpha;
varying vec2 textureCoordinate;
varying float vPhase;

void main(){
    vPhase = phase;
    gl_Position = vMatrix*vPosition;
    textureCoordinate = vCoord;
    vAlpha=alpha;
}