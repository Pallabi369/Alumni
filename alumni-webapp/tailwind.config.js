const defaultTheme = require('tailwindcss/defaultTheme')


module.exports = {
  mode: 'jit',
  content: [
    "./src/**/*.{js,jsx,ts,tsx}",
    './public/**/*.html',
  ],
  theme: {
    extend: {
      screens: {
        'xs': '355px',
        ...defaultTheme.screens,
      },
      boxShadow: {
        '3xl': '0 2px 12px 0 #EDEDF4',
        ...defaultTheme.boxShadow,
      },
      fontFamily: {
        sans: ['Proxima Nova', ...defaultTheme.fontFamily.sans],
        serif: ['Playfair Display', ...defaultTheme.fontFamily.serif],
        mono: ['Manrope', ...defaultTheme.fontFamily.mono],
      },
      height: {
        '56': '14rem',
        ...defaultTheme.height,
      },
      colors: {
        gray: {
          '900': '#2c3538',
          '800': '#575756',
          '700': '#898989',
          '300': '#E5E5E5'
        },
        orange: {
          500: '#E75A25',
          ...defaultTheme.colors.orange,
        },
        blue: {
          100: '#F2FBFE',
          200: '#b8ebff',
          400: '#18b2e8',
          500: '#0063db',
          700: '#004f9e',
          800: '#17245f',
          ...defaultTheme.colors.blue,
        },
      },
      blur: {
        xs: '2px',
      }
    }
  },
  plugins: [
    require('@tailwindcss/forms'),
    require('@tailwindcss/aspect-ratio'),
  ],
}
