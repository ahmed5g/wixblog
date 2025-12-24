/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}",
  ],
  theme: {
    extend: {
      fontFamily: {
        label: ['sohne', 'Helvetica Neue', 'Helvetica', 'Arial', 'sans-serif'],
      },
      colors: {

        label: '#242424',
      },
      variants: {
        extend: {
          cursor: ['disabled'],
          opacity: ['disabled'],
          backgroundColor: ['disabled', 'hover'],
        }
      },
    },
  },
  plugins: [require("daisyui")],
}
