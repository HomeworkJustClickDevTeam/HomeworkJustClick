/** @type {import('tailwindcss').Config} */
const colors = require('tailwindcss/colors')
module.exports = {
  content: ['./src/**/*.{js,jsx,ts,tsx}'],
  theme: {
    colors:{
      'main_blue': '#6D8DFF',
      'hover_blue' : '#7896FF',
      'hover_gray' : '#eff0f4',
      'font_gray' : '#49454F',
      white: colors.white,
      'light_gray': '#9ca3af',
      'lilly-bg' : 'rgba(109, 141, 255, 0.12)',
      'berry_red': '#990000',
      black: colors.black,
      current: 'currentColor',
      'colorsArray':['#59007f', '#006400']
    },
    extend: {
      fontFamily: {
        'lato': ['Inter', 'sans-serif']
      }
    },
  },
  plugins: [],
}

