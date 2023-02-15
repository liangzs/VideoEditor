#version 100
precision mediump float;
#define PI 3.14159265358;

varying vec2 resolution;
varying vec2 center;
varying vec3 color;
varying float progress;

float clam(float x, float lowerlimit, float upperlimit) {
    if (x < lowerlimit)
    x = lowerlimit;
    if (x > upperlimit)
    x = upperlimit;
    return x;
}

float smoothste(float edge0, float edge1, float x) {
    x = clam((x - edge0) / (edge1 - edge0), 0.0, 1.0);
    return x * x * (3. - 2. * x);
}

vec2 normaliz(vec2 a) {
    float sum = a.x + a.y;
    return vec2(a.x/sum, a.y/sum);
}

float fra(float a) {
    if (a < 0.0) {
        return mod(a, 1.0) + 1.0;
    } else {
        return mod(a, 1.0);
    }
}

vec3 frac(vec3 a) {
    return vec3(fra(a.x), fra(a.y), fra(a.z));
}

void main()
{
    vec2 uv = gl_FragCoord.xy / resolution.xy;
    uv.x -= .5;
    uv.x *= resolution.x/resolution.y;


    vec2 p = center * 0.5 + 0.5;
    p.x -= .5;
    p.x *= 1.6;

    vec3 colorResult = vec3(0.);
    vec3 baseColor = color;
    for(float i=0.; i < 127.0; i++) {
        vec3 p3 = frac(vec3(i) * vec3(.1101,.1301,.1501));
        p3 += dot(p3, p3.yzx + 19.19);
        vec3 n =  frac(vec3((p3.x + p3.y)*p3.z, (p3.x+p3.z)*p3.y, (p3.y+p3.z)*p3.x)) - .5;

        vec2 startP = p-vec2(0., progress*progress*.1);
        vec2 endP = startP+normaliz(n.xy)*n.z;


        float pt = 1.-pow(progress-1., 2.);
        vec2 pos = mix(p, endP, pt);
        float size = mix(.01, .005, smoothste(0., .1, pt));
        size *= smoothste(1., .1, pt);

        float sparkle = (sin((pt+n.z)*100.)*.5+.5);
        sparkle = pow(sparkle, pow(color.x, 3.)*50.)*mix(0.01, .01, color.y*n.y);

        size += sparkle*smoothste(color.x-color.z, color.x+color.z, progress)*smoothste(color.y+color.z, color.y-color.z, progress);
        colorResult += baseColor*(size * size/dot(uv - pos, uv - pos));
    }


    if (colorResult.x > 0.0 && colorResult.y > 0.0 && colorResult.z > 0.0) {
        gl_FragColor = vec4(colorResult, 1.);
    }

}