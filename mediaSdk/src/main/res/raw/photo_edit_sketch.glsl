varying highp vec2 textureCoordinate;
precision highp float;
uniform sampler2D inputImageTexture;
uniform vec2 singleStepOffset;
uniform float strength;

const highp vec3 W = vec3(0.299, 0.587, 0.114);

void main()
{
    float threshold = 0.0;
    vec4 oralColor = texture2D(inputImageTexture, textureCoordinate);
    float resultAlpha=oralColor.a*(1.0/(oralColor.a+0.00001));
    vec3 maxValue = vec3(0., 0., 0.);

    for (int i = -2; i<=2; i++)
    {
        for (int j = -2; j<=2; j++)
        {
            vec4 tempColor = texture2D(inputImageTexture, textureCoordinate+singleStepOffset*vec2(i, j));
            maxValue.r = max(maxValue.r, tempColor.r);
            maxValue.g = max(maxValue.g, tempColor.g);
            maxValue.b = max(maxValue.b, tempColor.b);
            threshold += dot(tempColor.rgb, W);
        }
    }
    float gray1 = dot(oralColor.rgb, W);

    float gray2 = dot(maxValue, W);

    float contour = gray1 / gray2;

    threshold = threshold / 25.0;
    float alpha = gray1>threshold?1.0:(gray1/threshold);

    float result = contour * alpha + (1.0-alpha)*gray1;

    gl_FragColor = mix(oralColor, vec4(result, result, result, oralColor.w)*resultAlpha, strength);
} 