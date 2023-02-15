#version 100
precision mediump float;
varying vec2 textureCoordinate;
uniform float progress;
uniform sampler2D vTexture;
uniform sampler2D vTexture1;

float persp=0.7;// = 0.7
float unzoom= 0.3;// = 0.3
float reflection = 0.4;// = 0.4
float floating= 3.0;// = 3.0

vec2 project (vec2 p) {
    return p * vec2(1.0, -1.2) + vec2(0.0, -floating/100.);
}

bool inBounds (vec2 p) {
    return all(lessThan(vec2(0.0), p)) && all(lessThan(p, vec2(1.0)));
}

vec4 bgColor (vec2 p, vec2 pfr, vec2 pto) {
    vec4 c = vec4(0.0, 0.0, 0.0, 1.0);
    pfr = project(pfr);
    // FIXME avoid branching might help perf!
    if (inBounds(pfr)) {
        c += mix(vec4(0.0), texture2D(vTexture1, pfr), reflection * mix(1.0, 0.0, pfr.y));
    }
    pto = project(pto);
    if (inBounds(pto)) {
        c += mix(vec4(0.0), texture2D(vTexture, pto), reflection * mix(1.0, 0.0, pto.y));
    }
    return c;
}

vec2 xskew (vec2 p, float persp, float center) {
    float x = mix(p.x, 1.0-p.x, center);
    return (
    (
    vec2(x, (p.y - 0.5*(1.0-persp) * x) / (1.0+(persp-1.0)*x))
    - vec2(0.5-distance(center, 0.5), 0.0)
    )
    * vec2(0.5 / distance(center, 0.5) * (center<0.5 ? 1.0 : -1.0), 1.0)
    + vec2(center<0.5 ? 0.0 : 1.0, 0.0)
    );
}
void main() {
    float uz = unzoom * 2.0*(0.5-distance(0.5, progress));
    vec2 p = -uz*0.5+(1.0+uz) * textureCoordinate;
    vec2 fromP = xskew(
    (p - vec2(progress, 0.0)) / vec2(1.0-progress, 1.0),
    1.0-mix(progress, 0.0, persp),
    0.0
    );
    vec2 toP = xskew(
    p / vec2(progress, 1.0),
    mix(pow(progress, 2.0), 1.0, persp),
    1.0
    );
    // FIXME avoid branching might help perf!
    if (inBounds(fromP)) {
        gl_FragColor= texture2D(vTexture1, fromP);
        return;
    }
    if (inBounds(toP)) {
        gl_FragColor= texture2D(vTexture, toP);
        return;
    }
    gl_FragColor= bgColor(textureCoordinate, fromP, toP);
}