/** @type {import('tailwindcss').Config} */
const colors = require('tailwindcss/colors')
module.exports = {
  content: ['./src/**/*.{js,jsx,ts,tsx}'],
  theme: {
    colors:{
      'main_blue': '#6D8DFF',
      white: colors.white,
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

