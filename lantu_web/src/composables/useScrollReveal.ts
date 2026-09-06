import { onMounted, onUnmounted } from 'vue'

export function useScrollReveal(
  rootMargin = '0px 0px -80px 0px',
  threshold = 0.1,
) {
  let observer: IntersectionObserver | null = null
  let mutationObserver: MutationObserver | null = null

  function observeRevealElements() {
    if (!observer) return
    document.querySelectorAll('.reveal:not(.revealed)').forEach((el) => {
      observer!.observe(el)
    })
  }

  onMounted(() => {
    observer = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            entry.target.classList.add('revealed')
            observer?.unobserve(entry.target)
          }
        })
      },
      { rootMargin, threshold },
    )

    // Initial observation
    observeRevealElements()

    // Watch for dynamically added .reveal elements (lazy-loaded routes)
    mutationObserver = new MutationObserver(() => {
      observeRevealElements()
    })

    mutationObserver.observe(document.body, {
      childList: true,
      subtree: true,
    })
  })

  onUnmounted(() => {
    observer?.disconnect()
    observer = null
    mutationObserver?.disconnect()
    mutationObserver = null
  })
}
