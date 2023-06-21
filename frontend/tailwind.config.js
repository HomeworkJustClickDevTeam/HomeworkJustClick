/** @type {import('tailwindcss').Config} */
const colors = require('tailwindcss/colors')
module.exports = {
  content: ['./src/**/*.{js,jsx,ts,tsx}'],
  theme: {
    colors:{
      'main_blue': '#6D8DFF',
      'hover_blue' : '#8ba4fc',
      white: colors.white,
      'light_gray': '#9ca3af',
      'lilly-bg' : 'rgba(109, 141, 255, 0.12)',
      black: colors.black,
      current: 'currentColor',
    },
    extend: {
      fontFamily: {
        'lato': ['Inter', 'sans-serif']
      }
    },
  },
  plugins: [],
}

