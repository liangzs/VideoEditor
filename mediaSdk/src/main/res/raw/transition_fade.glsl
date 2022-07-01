precision mediump float;
varying vec2 textureCoordinate;
uniform float progress;
uniform sampler2D vTexture;
uniform sampler2D vTexture1;
//progress不能大于一
void main(){
    vec4 color = mix(texture2D(vTexture1, textureCoordinate), texture2D(vTexture, textureCoordinate), progress);
    gl_FragColor = vec4(color);
}