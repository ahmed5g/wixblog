/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}",
  ],
  theme: {
    extend: {
      variants: {
        extend: {
          cursor: ['disabled'],
          opacity: ['disabled'],
          backgroundColor: ['disabled', 'hover'],
        }
      }
    },
  },
  plugins: [require("daisyui")],
}
