precision mediump float;
varying vec2 textureCoordinate;
uniform sampler2D vTexture;
uniform float intensity;

void main() {
  vec4 src = texture2D(vTexture, textureCoordinate);
  float gray = (src.r + src.g + src.b) / 3.0;
  vec3 v_gray=vec3(gray, gray, gray);
  gl_FragColor =vec4(mix(v_gray, src.rgb, intensity),1.0);
}