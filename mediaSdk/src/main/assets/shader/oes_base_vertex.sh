attribute vec4 vPosition;
attribute vec2 vCoord;
uniform mat4 vMatrix;
varying vec2 textureCoordinate;
uniform float alpha;
varying float vAlpha;

void main(){
    gl_Position = vMatrix*vPosition;
    textureCoordinate = vCoord;
    vAlpha=alpha;
}