<template>
  <nav class="art-steps" aria-label="操作进度" :style="{ '--step-count': steps.length }">
    <ol class="art-steps__list">
      <li
        v-for="(step, index) in steps"
        :key="step.key"
        class="art-steps__item"
        :class="{
          'is-finished': index < current,
          'is-current': index === current,
          'is-pending': index > current
        }"
        :aria-current="index === current ? 'step' : undefined"
      >
        <div class="art-steps__track">
          <span class="art-steps__node" aria-hidden="true">{{ index + 1 }}</span>
          <span v-if="index < steps.length - 1" class="art-steps__line" aria-hidden="true" />
        </div>
        <span class="art-steps__title">{{ step.title }}</span>
      </li>
    </ol>
  </nav>
</template>

<script setup lang="ts">
  defineOptions({ name: 'ArtSteps' })

  interface StepItem {
    key: string
    title: string
  }

  interface Props {
    steps: readonly StepItem[]
    current?: number
  }

  withDefaults(defineProps<Props>(), {
    current: 0
  })
</script>

<style scoped lang="scss">
  .art-steps {
    width: 100%;
    min-width: 520px;
    padding: 4px 0;
  }

  .art-steps__list {
    display: grid;
    grid-template-columns: repeat(var(--step-count), minmax(0, 1fr));
    padding: 0;
    margin: 0;
    list-style: none;
  }

  .art-steps__item {
    min-width: 0;
    color: var(--el-text-color-placeholder);
  }

  .art-steps__track {
    position: relative;
    display: flex;
    justify-content: center;
  }

  .art-steps__node {
    position: relative;
    z-index: 1;
    display: inline-flex;
    align-items: center;
    justify-content: center;
    width: 36px;
    height: 36px;
    font-size: 13px;
    font-weight: 600;
    line-height: 1;
    color: var(--el-text-color-placeholder);
    background-color: var(--el-bg-color);
    border: 2px solid var(--el-border-color);
    border-radius: 50%;
    transition:
      color 0.2s ease,
      background-color 0.2s ease,
      border-color 0.2s ease,
      box-shadow 0.2s ease;
  }

  .art-steps__line {
    position: absolute;
    top: 17px;
    left: calc(50% + 24px);
    width: calc(100% - 48px);
    height: 3px;
    background-color: var(--el-border-color-lighter);
    transition: background-color 0.2s ease;
  }

  .art-steps__title {
    display: block;
    margin-top: 8px;
    overflow: hidden;
    font-size: 12px;
    font-weight: 600;
    line-height: 20px;
    color: currentcolor;
    text-align: center;
    text-overflow: ellipsis;
    white-space: nowrap;
    transition: color 0.2s ease;
  }

  .art-steps__item.is-finished,
  .art-steps__item.is-current {
    color: var(--el-color-primary);
  }

  .art-steps__item.is-finished {
    .art-steps__node,
    .art-steps__line {
      color: var(--el-color-white);
      background-color: var(--el-color-primary);
      border-color: var(--el-color-primary);
    }
  }

  .art-steps__item.is-current .art-steps__node {
    color: var(--el-color-white);
    background-color: var(--el-color-primary);
    border-color: var(--el-color-primary);
    box-shadow: 0 0 0 5px var(--el-color-primary-light-8);
  }

  @media (width <= 768px) {
    .art-steps__node {
      width: 32px;
      height: 32px;
      font-size: 12px;
    }

    .art-steps__line {
      top: 15px;
      left: calc(50% + 22px);
      width: calc(100% - 44px);
    }

    .art-steps__title {
      font-size: 12px;
    }
  }
</style>
