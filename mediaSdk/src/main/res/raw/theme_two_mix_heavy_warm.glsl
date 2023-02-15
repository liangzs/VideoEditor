precision mediump float;
varying vec2 textureCoordinate;
uniform sampler2D vTexture;
uniform float intensity;

void modifyColor(vec4 color){
    color.r=max(min(color.r,1.0),0.0);
    color.g=max(min(color.g,1.0),0.0);
    color.b=max(min(color.b,1.0),0.0);
    color.a=max(min(color.a,1.0),0.0);
}
void main() {
  vec4 src = texture2D(vTexture, textureCoordinate);
  vec3 u_ChangeColor=vec3(0.2,0.2,0.0);
  vec4 deltaColor=src+vec4(u_ChangeColor,0.0);
  modifyColor(deltaColor);
  gl_FragColor =vec4(mix(deltaColor.rgb, src.rgb, intensity),1.0);
}