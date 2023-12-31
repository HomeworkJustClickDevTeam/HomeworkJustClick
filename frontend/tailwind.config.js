/** @type {import('tailwindcss').Config} */
const colors = require('tailwindcss/colors')
module.exports = {
    content: ['./src/**/*.{js,jsx,ts,tsx}'],
    theme: {
        colors: {
            'main_blue': '#6D8DFF',
            'main_lily': '#8A80FA',
            'hover_blue': '#7896FF',
            'hover_lily': '#938AFA',
            'hover_gray': '#eff0f4',
            'font_gray': '#49454F',
            'border_gray': '#787878',
            white: colors.white,
            'light_gray': '#9ca3af',
            'lilly-bg': 'rgba(109, 141, 255, 0.12)',
            'berry_red': '#990000',
            'dark_grey': '#504F51',
            'scarlet': '#FF2400',
            black: colors.black,
            current: 'currentColor',
            'colorsArray': ['bg-[#59007f]', 'bg-[#006400]', 'bg-[#800000]', 'bg-[#000000]', 'bg-[#469990]', 'bg-[#000075]', 'bg-[#f58231]', 'bg-[#546A7B]', 'bg-[#000000]', 'bg-[#BB9F06]', 'bg-[#3F2A2B]', 'bg-[#565656]', 'bg-[#99621E]', 'bg-[#739E82]', 'bg-[#550C18]', 'bg-[#136F63]', 'bg-[#EF7A85]', 'bg-[#D664BE]', 'bg-[#642CA9]', 'bg-[#03312E]']
        },
        extend: {
            fontFamily: {
                'lato': ['Inter', 'sans-serif']
            },
            backgroundImage: {
                'notes_img': "url('./images/zadanie.png')",
                'notes_img2': "url('./images/zad2.png')"
            }
        },
    },
    plugins: [],
}

